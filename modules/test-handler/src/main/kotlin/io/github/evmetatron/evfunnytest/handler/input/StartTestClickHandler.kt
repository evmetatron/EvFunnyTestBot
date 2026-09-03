package io.github.evmetatron.evfunnytest.handler.input

import io.github.evmetatron.evfunnytest.dto.adapter.InputAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import io.github.evmetatron.evfunnytest.dto.button.StartTestButton
import io.github.evmetatron.evfunnytest.dto.button.toConcreteButtonAs
import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.exception.InputHandlerNotFoundException
import io.github.evmetatron.evfunnytest.exception.TestHandlerNotFoundException
import io.github.evmetatron.evfunnytest.service.CurrentTestService
import io.github.evmetatron.evfunnytest.service.TestService
import io.github.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Suppress("MagicNumber")
@Order(5)
@Component
class StartTestClickHandler(
    private val testService: TestService,
    private val currentTestService: CurrentTestService,
) : AbstractInputHandler() {
    companion object {
        const val TEST_NOT_FOUND = "Тест не найден"
    }

    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean {
        val isEmptyCurrentTest = currentTestEntity == null
        val isStartTest = inputAdapter.button?.type == ButtonType.START_TEST

        return isEmptyCurrentTest && isStartTest
    }

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter {
        val startTestButton = inputAdapter.button.toConcreteButtonAs<StartTestButton>()

        if (inputHandler == null) {
            throw TestHandlerNotFoundException()
        }

        val test = testService.getTest(startTestButton.testId)
            ?: return inputAdapter.toSendMessage(TEST_NOT_FOUND)

        val createdCurrentTest = currentTestService.createCurrentTest(inputAdapter.user.id, test)

        return inputHandler.getObject(inputAdapter, createdCurrentTest, context.withHandledStart())
            ?: throw InputHandlerNotFoundException()
    }
}
