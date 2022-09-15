/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.ButtonClick
import com.evmetatron.evfunnytest.dto.GetTestClick
import com.evmetatron.evfunnytest.dto.StartTestClick
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.fixtures.createCallbackQuery
import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import com.evmetatron.evfunnytest.fixtures.createTestEntity
import com.evmetatron.evfunnytest.fixtures.createUpdate
import com.evmetatron.evfunnytest.fixtures.faker
import com.evmetatron.evfunnytest.storage.db.repository.TestRepository
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.utils.getChat
import com.evmetatron.evfunnytest.utils.toSendMessage
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.data.repository.findByIdOrNull
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.EntityType
import org.telegram.telegrambots.meta.api.objects.MessageEntity
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class GetTestClickHandlerTest {
    @MockK
    private lateinit var testRepository: TestRepository

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    private lateinit var getTestClickHandler: GetTestClickHandler

    @ParameterizedTest
    @MethodSource("verifyFalseProvider")
    fun `verify false`(update: Update, currentTestEntity: CurrentTestEntity?) {
        val sendMessage = SendMessage().apply {
            text = faker.harryPotter().quote()
        }

        every { inputHandler.execute(update, currentTestEntity) } returns sendMessage

        getTestClickHandler.execute(update, currentTestEntity) shouldBe sendMessage

        verify(exactly = 1) { inputHandler.execute(update, currentTestEntity) }
    }

    @Test
    fun `success execute`() {
        val testId = 55L
        val update = createUpdate(
            message = null,
            callbackQuery = createCallbackQuery(
                data = GetTestClick(testId = testId).toButtonClick().toJson(),
            ),
        )
        val test = createTestEntity()

        val currentTestEntity = null

        every {
            testRepository.findByIdOrNull(testId)
        } returns test

        val text = "${test.name}\n\n${test.description}"

        val expected = SendMessage().apply {
            this.text = text
            this.chatId = update.getChat().id.toString()
            this.entities = listOf(
                MessageEntity().apply {
                    this.type = EntityType.BOLD
                    this.text = test.name
                    this.offset = 0
                    this.length = test.name.length
                },
                MessageEntity().apply {
                    this.type = EntityType.ITALIC
                    this.text = test.description
                    this.offset = text.length - test.description.length
                    this.length = test.description.length
                },
            )
            this.replyMarkup = InlineKeyboardMarkup().apply {
                this.keyboard = listOf(
                    listOf(
                        InlineKeyboardButton().apply {
                            this.text = GetTestClickHandler.BUTTON_TEXT
                            this.callbackData = StartTestClick(
                                testId = test.id,
                            ).toButtonClick().toJson()
                        }
                    ),
                )
            }
        }

        getTestClickHandler.execute(update, currentTestEntity) shouldBe expected

        verify(exactly = 0) { inputHandler.execute(update, currentTestEntity) }
    }

    @Test
    fun `fail execute - test not found`() {
        val testId = 55L
        val update = createUpdate(
            message = null,
            callbackQuery = createCallbackQuery(
                data = GetTestClick(testId = testId).toButtonClick().toJson(),
            ),
        )

        val currentTestEntity = null

        every {
            testRepository.findByIdOrNull(testId)
        } returns null

        val expected = update.toSendMessage(GetTestClickHandler.TEST_NOT_FOUND)

        getTestClickHandler.execute(update, currentTestEntity) shouldBe expected

        verify(exactly = 0) { inputHandler.execute(update, currentTestEntity) }
    }

    private fun verifyFalseProvider() =
        listOf(
            // Существует CurrentTestEntity
            Arguments.of(
                createUpdate(
                    message = null,
                    callbackQuery = createCallbackQuery(
                        data = ButtonClick(type = ButtonType.START_TEST, data = emptyMap()).toJson(),
                    ),
                ),
                createCurrentTestEntity(),
            ),
            // Клик на кнопку не соответствует событию начала теста
            Arguments.of(
                createUpdate(
                    message = null,
                    callbackQuery = createCallbackQuery(
                        data = ButtonClick(type = ButtonType.PAGE, data = emptyMap()).toJson()
                    )
                ),
                null,
            ),
        )
}
