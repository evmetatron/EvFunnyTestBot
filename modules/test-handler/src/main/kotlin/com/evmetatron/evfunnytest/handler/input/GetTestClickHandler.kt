/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import com.evmetatron.evfunnytest.dto.button.GetTestButton
import com.evmetatron.evfunnytest.dto.button.StartTestButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.service.TestService
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

class GetTestClickHandler(
    private val testService: TestService,
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
    companion object {
        const val BUTTON_TEXT = "Начать тест"
        const val TEST_NOT_FOUND = "Тест не найден"
    }

    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean {
        val isEmptyCurrentTest = currentTestEntity == null
        val isGetTest = inputAdapter.button?.type == ButtonType.GET_TEST

        return isEmptyCurrentTest && isGetTest
    }

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter {
        val getTestButton = inputAdapter.button?.toConcreteButton() as GetTestButton

        val test = testService.getTest(getTestButton.testId)
            ?: return inputAdapter.toSendMessage(TEST_NOT_FOUND)

        return SendMessageAdapter(
            chatId = inputAdapter.chatId,
            text = "[b]${test.name}[/b]\n\n[i]${test.description}[/i]",
            buttons = listOf(
                listOf(
                    ButtonAdapter(
                        text = BUTTON_TEXT,
                        button = StartTestButton(
                            testId = test.id,
                        ).toBaseButton(),
                    ),
                ),
            ),
        )
    }
}
