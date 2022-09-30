/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import com.evmetatron.evfunnytest.dto.adapter.EditButtonsAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.textselection.DefaultSelection
import com.evmetatron.evfunnytest.dto.button.GetTestButton
import com.evmetatron.evfunnytest.dto.button.PageButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.service.TestService

class ListCommandHandler(
    private val testService: TestService,
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
    companion object {
        const val DEFAULT_LIMIT = 8
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_CHUNK = 2
        const val HELLO_TEXT = """
                Привет {user}
                
                Я бот с тестами.
                
                Можешь выбрать любой тест и пройти его
            """
    }

    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean {
        val isEmptyCurrentTest = currentTestEntity == null
        val isListCommand = inputAdapter.command?.let { it == BotCommand.START || it == BotCommand.LIST } ?: false
        val isClickPage = inputAdapter.button?.type == ButtonType.PAGE

        return isEmptyCurrentTest && (isListCommand || isClickPage)
    }

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter {
        val pageButton = inputAdapter.button?.toConcreteButton() as PageButton?

        val offset = pageButton?.offset ?: DEFAULT_OFFSET

        val tests = testService.findTests(DEFAULT_LIMIT + 1, offset)

        val hasNext = tests.size > DEFAULT_LIMIT

        val text = HELLO_TEXT
            .replace("{user}", inputAdapter.user.toName())
            .trimIndent()

        val buttons = tests.take(DEFAULT_LIMIT).chunked(DEFAULT_CHUNK)
            .map { chunkTests ->
                chunkTests.map { test ->
                    ButtonAdapter(
                        text = test.name,
                        button = GetTestButton(
                            testId = test.id,
                        ).toBaseButton(),
                    )
                }
            } + listOf(
            listOfNotNull(
                offset.takeIf { it > 0 }
                    ?.let {
                        ButtonAdapter(
                            text = "⏮",
                            button = PageButton(
                                offset = offset - DEFAULT_LIMIT,
                            ).toBaseButton(),
                        )
                    },
                hasNext.takeIf { it }
                    ?.let {
                        ButtonAdapter(
                            text = "⏭",
                            button = PageButton(
                                offset = offset + DEFAULT_LIMIT,
                            ).toBaseButton(),
                        )
                    },
            ),
        )

        return if (pageButton == null) {
            SendMessageAdapter(
                chatId = inputAdapter.chatId,
                text = listOf(DefaultSelection(text = text)),
                buttons = buttons,
            )
        } else {
            EditButtonsAdapter(
                chatId = inputAdapter.chatId,
                messageId = inputAdapter.messageId,
                buttons = buttons,
            )
        }
    }
}
