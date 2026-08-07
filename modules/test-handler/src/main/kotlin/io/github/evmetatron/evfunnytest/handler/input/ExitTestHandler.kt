package io.github.evmetatron.evfunnytest.handler.input

import io.github.evmetatron.evfunnytest.dto.adapter.InputAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.enumerable.BotCommand
import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.service.CurrentTestService
import io.github.evmetatron.evfunnytest.service.TestService
import io.github.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(2)
@Component
class ExitTestHandler(
    private val currentTestService: CurrentTestService,
    private val testService: TestService,
) : AbstractInputHandler() {
    companion object {
        const val TEST_NOT_STARTED_TEXT = "Запущенных тестов не найдено"
        const val TEST_EXIT_TEXT = "Тест \"{test}\" был завершен"
    }

    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean {
        val isExitCommand = inputAdapter.command == BotCommand.EXIT
        val isExitButton = inputAdapter.button?.type == ButtonType.EXIT_TEST

        return isExitCommand || isExitButton
    }

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter {
        if (currentTestEntity == null) {
            return inputAdapter.toSendMessage(TEST_NOT_STARTED_TEXT)
        }

        val test = testService.getTest(currentTestEntity.testId)

        currentTestService.removeCurrentTest(currentTestEntity.userId)

        return inputAdapter.toSendMessage(TEST_EXIT_TEXT.replace("{test}", test?.name ?: ""))
    }
}
