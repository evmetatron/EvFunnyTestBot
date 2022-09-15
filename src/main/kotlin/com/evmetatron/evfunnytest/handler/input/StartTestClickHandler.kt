/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.button.StartTestButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.exception.InputHandlerNotFoundException
import com.evmetatron.evfunnytest.exception.TestHandlerNotFoundException
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.service.TestService
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

class StartTestClickHandler(
    private val testService: TestService,
    private val currentTestService: CurrentTestService,
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
    companion object {
        const val TEST_NOT_FOUND = "Тест не найден"
    }

    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean {
        val isEmptyCurrentTest = currentTestEntity == null
        val isStartTest = inputAdapter.button?.let { it.type == ButtonType.START_TEST } ?: false

        return isEmptyCurrentTest && isStartTest
    }

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter {
        val startTestButton = inputAdapter.button?.toConcreteButton() as StartTestButton

        if (inputHandler == null) {
            throw TestHandlerNotFoundException()
        }

        val test = testService.getTest(startTestButton.testId)
            ?: return inputAdapter.toSendMessageDefault(TEST_NOT_FOUND)

        val createdCurrentTest = currentTestService.createCurrentTest(inputAdapter.user.id, test)

        return inputHandler.getObject(inputAdapter, createdCurrentTest, context.withHandledStart())
            ?: throw InputHandlerNotFoundException()
    }
}
