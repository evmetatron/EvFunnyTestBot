/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler

import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import com.evmetatron.evfunnytest.fixtures.createMainProperties
import com.evmetatron.evfunnytest.fixtures.createUpdate
import com.evmetatron.evfunnytest.fixtures.faker
import com.evmetatron.evfunnytest.handler.input.InputHandler
import com.evmetatron.evfunnytest.property.MainProperties
import com.evmetatron.evfunnytest.storage.memory.repository.CurrentTestRepository
import com.evmetatron.evfunnytest.utils.getUser
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@ExtendWith(MockKExtension::class)
internal class BotHandlerTest {
    @SpyK
    private var mainProperties: MainProperties = createMainProperties()

    @MockK
    private lateinit var currentTestRepository: CurrentTestRepository

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    @SpyK
    private lateinit var botHandler: BotHandler

    @Test
    fun `success onUpdateReceived - SendMessage`() {
        val event = SendMessage().apply {
            this.chatId = faker.number().toString()
            this.replyToMessageId = faker.number().randomDigit()
            this.text = faker.harryPotter().quote()
        }

        val update = createUpdate()

        val currentTest = createCurrentTestEntity()

        val message: Message = mockk()

        every { botHandler.execute(event) } returns message

        every { currentTestRepository.findByIdOrNull(update.getUser().id) } returns currentTest

        every { inputHandler.execute(update, currentTest) } returns event

        botHandler.onUpdateReceived(update)

        verify(exactly = 1) { botHandler.execute(event) }
    }

    @Test
    fun `success onUpdateReceived - EditMessageReplyMarkup`() {
        val event = EditMessageReplyMarkup().apply {
            this.chatId = faker.number().toString()
            this.messageId = faker.number().randomDigit()
            this.replyMarkup = InlineKeyboardMarkup().apply {
                (1..3).map {
                    (1..3).map {
                        InlineKeyboardButton().apply {
                            this.text = faker.harryPotter().quote()
                            this.callbackData = faker.harryPotter().book()
                        }
                    }
                }
            }
        }

        val update = createUpdate()

        val currentTest = createCurrentTestEntity()

        val message: Message = mockk()

        every { botHandler.execute(event) } returns message

        every { currentTestRepository.findByIdOrNull(update.getUser().id) } returns currentTest

        every { inputHandler.execute(update, currentTest) } returns event

        botHandler.onUpdateReceived(update)

        verify(exactly = 1) { botHandler.execute(event) }
    }
}
