/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package handler

import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.handler.BotHandler
import fixtures.createCurrentTestEntity
import fixtures.createEditButtonsAdapter
import fixtures.createRemoveButtonsEntity
import fixtures.createSendMessageAdapter
import fixtures.createUpdate
import fixtures.faker
import com.evmetatron.evfunnytest.handler.input.InputHandler
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.service.RemoveButtonsService
import com.evmetatron.evfunnytest.utils.toInputAdapter
import com.evmetatron.evfunnytest.utils.toTelegramMessage
import fixtures.createTelegramProperties
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Message

@ExtendWith(MockKExtension::class)
internal class BotHandlerTest {
    @SpyK
    private var telegramProperties = createTelegramProperties()

    @MockK
    private lateinit var currentTestService: CurrentTestService

    @MockK(relaxed = true)
    private lateinit var removeButtonsService: RemoveButtonsService

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    @SpyK
    private lateinit var botHandler: BotHandler

    @Test
    fun `success onUpdateReceived - SendMessage with clear buttons logic`() {
        val sendMessageAdapter = createSendMessageAdapter(clearButtonsLater = true)
        val sendMessage = sendMessageAdapter.toTelegramMessage() as SendMessage

        val update = createUpdate()

        val inputAdapter = update.toInputAdapter()

        val context = HandlerContext()

        val currentTest = createCurrentTestEntity(userId = inputAdapter.user.id)

        val message: Message = Message().apply {
            messageId = faker.number().randomDigit()
        }

        val removeButtonsEntity = createRemoveButtonsEntity(
            userId = inputAdapter.user.id,
            messageIds = listOf(1, 2, 3),
        )

        every { removeButtonsService.getByUserId(inputAdapter.user.id) } returns removeButtonsEntity

        every { botHandler.execute(sendMessage) } returns message

        every { botHandler.execute(not(sendMessage)) } returns Message()

        every { currentTestService.getCurrentTest(inputAdapter.user.id) } returns currentTest

        every { inputHandler.getObject(inputAdapter, currentTest, context) } returns sendMessageAdapter

        botHandler.onUpdateReceived(update)

        verify(exactly = 1) { botHandler.execute(sendMessage) }

        removeButtonsEntity.messageIds.forEach { messageId ->
            val editMessage = EditMessageReplyMarkup().apply {
                this.chatId = removeButtonsEntity.chatId.toString()
                this.messageId = messageId
                this.replyMarkup = null
            }
            coVerify(exactly = 1) { botHandler.execute(editMessage) }
        }

        coVerify(exactly = 1) { removeButtonsService.remove(removeButtonsEntity.userId) }

        verify(exactly = 1) {
            removeButtonsService.registerMessage(
                inputAdapter.user.id,
                inputAdapter.chatId,
                message.messageId,
            )
        }
    }

    @Test
    fun `success onUpdateReceived - EditMessageReplyMarkup without clear buttons logic`() {
        val editButtonsAdapter = createEditButtonsAdapter(clearButtonsLater = false)
        val editMarkup = editButtonsAdapter.toTelegramMessage() as EditMessageReplyMarkup

        val update = createUpdate()

        val inputAdapter = update.toInputAdapter()

        val context = HandlerContext()

        val currentTest = createCurrentTestEntity()

        val message: Message = Message().apply { this.messageId = faker.number().randomDigit() }

        every { removeButtonsService.getByUserId(inputAdapter.user.id) } returns null

        every { botHandler.execute(editMarkup) } returns message

        every { currentTestService.getCurrentTest(inputAdapter.user.id) } returns currentTest

        every { inputHandler.getObject(inputAdapter, currentTest, context) } returns editButtonsAdapter

        botHandler.onUpdateReceived(update)

        verify(exactly = 1) { botHandler.execute(editMarkup) }
        verify(exactly = 1) { botHandler.execute(any<EditMessageReplyMarkup>()) }
    }

    @Test
    fun `success clearButtonsSchedule`() {
        val expiredClearButtons = listOf(
            createRemoveButtonsEntity(userId = 1, chatId = 1, messageIds = listOf(1, 2, 3)),
            createRemoveButtonsEntity(userId = 2, chatId = 2, messageIds = listOf(1, 2, 3)),
        )

        coEvery { removeButtonsService.findExpired() } returns expiredClearButtons

        botHandler.clearButtonsSchedule()

        expiredClearButtons.forEach { removeButtons ->
            removeButtons.messageIds.forEach { messageId ->
                val editMessage = EditMessageReplyMarkup().apply {
                    this.chatId = removeButtons.chatId.toString()
                    this.messageId = messageId
                    this.replyMarkup = null
                }
                coVerify(exactly = 1) { botHandler.execute(editMessage) }
            }
        }
    }
}
