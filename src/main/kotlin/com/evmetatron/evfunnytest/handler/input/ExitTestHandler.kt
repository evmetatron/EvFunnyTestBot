/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.service.TestService
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

class ExitTestHandler(
    private val currentTestService: CurrentTestService,
    private val testService: TestService,
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
    companion object {
        const val TEST_NOT_STARTED_TEXT = "Запущенных тестов не найдено"
        const val TEST_EXIT_TEXT = "Тест \"{test}\" был завершен"
    }

    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext
    ): Boolean {
        val isExitCommand = inputAdapter.command == BotCommand.EXIT
        val isExitButton = inputAdapter.button?.type == ButtonType.EXIT_TEST

        return isExitCommand || isExitButton
    }

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext
    ): MessageAdapter {
        if (currentTestEntity == null) {
            return inputAdapter.toSendMessageDefault(TEST_NOT_STARTED_TEXT)
        }

        val test = testService.getTest(currentTestEntity.testId)

        currentTestService.removeCurrentTest(currentTestEntity.userId)

        return inputAdapter.toSendMessageDefault(TEST_EXIT_TEXT.replace("{test}", test?.name ?: ""))
    }
}
