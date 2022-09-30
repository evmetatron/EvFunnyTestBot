/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.textselection.DefaultSelection
import com.evmetatron.evfunnytest.dto.button.GetTestButton
import com.evmetatron.evfunnytest.dto.button.PageButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.enumerable.ButtonType
import fixtures.createBaseButton
import fixtures.createTestEntity
import fixtures.createCurrentTestEntity
import fixtures.createEditButtonsAdapter
import fixtures.createInputAdapter
import fixtures.createSendMessageAdapter
import com.evmetatron.evfunnytest.service.TestService
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(MockKExtension::class)
internal class ListCommandHandlerTest {
    @MockK
    private lateinit var testService: TestService

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    private lateinit var listCommandHandler: ListCommandHandler

    private companion object {
        @JvmStatic
        private fun verifyFalseProvider() =
            listOf(
                // Существует CurrentTestEntity
                Arguments.of(
                    createInputAdapter(
                        command = BotCommand.LIST,
                        button = null,
                    ),
                    createCurrentTestEntity(),
                ),

                // Нет нужной команды
                Arguments.of(
                    createInputAdapter(
                        command = BotCommand.EXIT,
                        button = null,
                    ),
                    null,
                ),

                // Клик на кнопку не соответствует событию пролистывания страниц
                Arguments.of(
                    createInputAdapter(
                        command = null,
                        button = createBaseButton(type = ButtonType.GET_TEST),
                    ),
                    null,
                ),
            )
    }

    @ParameterizedTest
    @MethodSource("verifyFalseProvider")
    fun `verify false`(inputAdapter: InputAdapter, currentTestEntity: CurrentTestEntity?) {
        val sendMessage = createSendMessageAdapter()
        val context = HandlerContext()

        every { inputHandler.getObject(inputAdapter, currentTestEntity, context) } returns sendMessage

        listCommandHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe sendMessage

        verify(exactly = 1) { inputHandler.getObject(inputAdapter, currentTestEntity, context) }
    }

    @ParameterizedTest
    @EnumSource(BotCommand::class, names = ["START", "LIST"])
    fun `success getObject - SendMessage`(command: BotCommand) {
        val inputAdapter = createInputAdapter(
            command = command,
            button = null,
        )
        val context = HandlerContext()

        val currentTestEntity = null

        val tests = (1..ListCommandHandler.DEFAULT_LIMIT + 1).map { createTestEntity() }

        every {
            testService.findTests(ListCommandHandler.DEFAULT_LIMIT + 1, ListCommandHandler.DEFAULT_OFFSET)
        } returns tests

        val expected = createSendMessageAdapter(
            chatId = inputAdapter.chatId,
            text = listOf(
                DefaultSelection(
                    text = ListCommandHandler.HELLO_TEXT.replace(
                        "{user}",
                        inputAdapter.user.toName(),
                    ).trimIndent()
                )
            ),
            buttons = tests.take(ListCommandHandler.DEFAULT_LIMIT).chunked(ListCommandHandler.DEFAULT_CHUNK)
                .map { chunkTests ->
                    chunkTests.map { test ->
                        ButtonAdapter(
                            text = test.name,
                            button = GetTestButton(testId = test.id).toBaseButton(),
                        )
                    }
                } + listOf(
                listOf(
                    ButtonAdapter(
                        text = "⏭",
                        button = PageButton(offset = ListCommandHandler.DEFAULT_LIMIT).toBaseButton(),
                    ),
                ),
            ),
        )

        listCommandHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 0) { inputHandler.getObject(any(), any(), any()) }
    }

    @Test
    fun `success getObject - EditMessageReplyMarkup`() {
        val offset = 15
        val inputAdapter = createInputAdapter(
            command = null,
            button = PageButton(offset = offset).toBaseButton(),
        )
        val context = HandlerContext()

        val currentTestEntity = null

        val tests = (1..ListCommandHandler.DEFAULT_LIMIT + 1).map { createTestEntity() }

        every {
            testService.findTests(ListCommandHandler.DEFAULT_LIMIT + 1, offset)
        } returns tests

        val expected = createEditButtonsAdapter(
            messageId = inputAdapter.messageId,
            chatId = inputAdapter.chatId,
            buttons = tests.take(ListCommandHandler.DEFAULT_LIMIT).chunked(ListCommandHandler.DEFAULT_CHUNK)
                .map { chunkTests ->
                    chunkTests.map { test ->
                        ButtonAdapter(
                            text = test.name,
                            button = GetTestButton(testId = test.id).toBaseButton(),
                        )
                    }
                } + listOf(
                listOf(
                    ButtonAdapter(
                        text = "⏮",
                        button = PageButton(offset = offset - ListCommandHandler.DEFAULT_LIMIT).toBaseButton(),
                    ),
                    ButtonAdapter(
                        text = "⏭",
                        button = PageButton(offset = offset + ListCommandHandler.DEFAULT_LIMIT).toBaseButton(),
                    ),
                ),
            ),
        )

        listCommandHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 0) { inputHandler.getObject(any(), any(), any()) }
    }
}
