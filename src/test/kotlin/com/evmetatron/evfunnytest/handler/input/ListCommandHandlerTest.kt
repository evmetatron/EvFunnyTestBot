/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.ButtonClick
import com.evmetatron.evfunnytest.dto.GetTestClick
import com.evmetatron.evfunnytest.dto.PageClick
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.fixtures.faker
import com.evmetatron.evfunnytest.fixtures.createUpdate
import com.evmetatron.evfunnytest.fixtures.createMessage
import com.evmetatron.evfunnytest.fixtures.createTestEntity
import com.evmetatron.evfunnytest.fixtures.createCallbackQuery
import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import com.evmetatron.evfunnytest.storage.db.repository.TestRepository
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.utils.getChat
import com.evmetatron.evfunnytest.utils.getUser
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
import org.junit.jupiter.params.provider.ValueSource
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class ListCommandHandlerTest {
    @MockK
    private lateinit var testRepository: TestRepository

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    private lateinit var listCommandHandler: ListCommandHandler

    @ParameterizedTest
    @MethodSource("verifyFalseProvider")
    fun `verify false`(update: Update, currentTestEntity: CurrentTestEntity?) {
        val sendMessage = SendMessage().apply {
            text = faker.harryPotter().quote()
        }

        every { inputHandler.execute(update, currentTestEntity) } returns sendMessage

        listCommandHandler.execute(update, currentTestEntity) shouldBe sendMessage

        verify(exactly = 1) { inputHandler.execute(update, currentTestEntity) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["/start", "/list"])
    fun `success execute - SendMessage`(command: String) {
        val update = createUpdate(
            message = createMessage(
                text = command,
            ),
            callbackQuery = null,
        )

        val currentTestEntity = null

        val tests = (1..ListCommandHandler.DEFAULT_LIMIT + 1).map { createTestEntity() }

        every {
            testRepository.findLimited(ListCommandHandler.DEFAULT_LIMIT + 1, ListCommandHandler.DEFAULT_OFFSET)
        } returns tests

        val expected = SendMessage().apply {
            text = ListCommandHandler.HELLO_TEXT
                .replace(
                    "{user}",
                    "${update.getUser().firstName} ${update.getUser().lastName}",
                )
            chatId = update.getChat().id.toString()
            replyMarkup = InlineKeyboardMarkup().apply {
                this.keyboard = tests.take(ListCommandHandler.DEFAULT_LIMIT).chunked(ListCommandHandler.DEFAULT_CHUNK)
                    .map { chunkTests ->
                        chunkTests.map { test ->
                            InlineKeyboardButton().apply {
                                this.text = test.name
                                this.callbackData = GetTestClick(
                                    testId = test.id,
                                ).toButtonClick().toJson()
                            }
                        }
                    } + listOf(
                    listOf(
                        InlineKeyboardButton().apply {
                            this.text = "⏭"
                            this.callbackData = PageClick(
                                offset = ListCommandHandler.DEFAULT_LIMIT,
                            ).toButtonClick().toJson()
                        },
                    ),
                )
            }
        }

        listCommandHandler.execute(update, currentTestEntity) shouldBe expected

        verify(exactly = 0) { inputHandler.execute(update, currentTestEntity) }
    }

    @Test
    fun `success execute - EditMessageReplyMarkup`() {
        val offset = 15
        val update = createUpdate(
            message = null,
            callbackQuery = createCallbackQuery(
                data = PageClick(offset = offset).toButtonClick().toJson()
            ),
        )

        val currentTestEntity = null

        val tests = (1..ListCommandHandler.DEFAULT_LIMIT + 1).map { createTestEntity() }

        every {
            testRepository.findLimited(ListCommandHandler.DEFAULT_LIMIT + 1, offset)
        } returns tests

        val expected = EditMessageReplyMarkup().apply {
            this.messageId = update.callbackQuery.message.messageId
            chatId = update.getChat().id.toString()
            replyMarkup = InlineKeyboardMarkup().apply {
                this.keyboard = tests.take(ListCommandHandler.DEFAULT_LIMIT).chunked(ListCommandHandler.DEFAULT_CHUNK)
                    .map { chunkTests ->
                        chunkTests.map { test ->
                            InlineKeyboardButton().apply {
                                this.text = test.name
                                this.callbackData = GetTestClick(
                                    testId = test.id,
                                ).toButtonClick().toJson()
                            }
                        }
                    } + listOf(
                    listOf(
                        InlineKeyboardButton().apply {
                            this.text = "⏮"
                            this.callbackData = PageClick(
                                offset = offset - ListCommandHandler.DEFAULT_LIMIT,
                            ).toButtonClick().toJson()
                        },
                        InlineKeyboardButton().apply {
                            this.text = "⏭"
                            this.callbackData = PageClick(
                                offset = offset + ListCommandHandler.DEFAULT_LIMIT,
                            ).toButtonClick().toJson()
                        },
                    ),
                )
            }
        }

        listCommandHandler.execute(update, currentTestEntity) shouldBe expected

        verify(exactly = 0) { inputHandler.execute(update, currentTestEntity) }
    }

    private fun verifyFalseProvider() =
        listOf(
            // Существует CurrentTestEntity
            Arguments.of(
                createUpdate(message = createMessage(text = "/list"), callbackQuery = null),
                createCurrentTestEntity(),
            ),

            // Нет нужной команды
            Arguments.of(
                createUpdate(message = createMessage(text = "/superpuper"), callbackQuery = null),
                null,
            ),

            // Клик на кнопку не соответствует событию пролистывания страниц
            Arguments.of(
                createUpdate(
                    message = null,
                    callbackQuery = createCallbackQuery(
                        data = ButtonClick(type = ButtonType.GET_TEST, data = emptyMap()).toJson()
                    )
                ),
                null,
            ),
        )
}
