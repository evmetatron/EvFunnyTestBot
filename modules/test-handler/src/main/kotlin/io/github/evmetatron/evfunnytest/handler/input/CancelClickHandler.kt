package io.github.evmetatron.evfunnytest.handler.input

import io.github.evmetatron.evfunnytest.dto.adapter.InputAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.exception.TestHandlerNotFoundException
import io.github.evmetatron.evfunnytest.service.CurrentTestService
import io.github.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(1)
@Component
class CancelClickHandler(
    private val currentTestService: CurrentTestService,
) : AbstractInputHandler() {
    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean {
        val isNotEmptyCurrentTest = currentTestEntity != null
        val isCancelAnswer = inputAdapter.button?.type == ButtonType.CANCEL_ANSWER

        return isNotEmptyCurrentTest && isCancelAnswer
    }

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter {
        val updatedCurrentTest = currentTestEntity!!.withoutAnswer()

        currentTestService.replaceCurrentTest(updatedCurrentTest)

        return inputHandler.getObject(inputAdapter, updatedCurrentTest, context)
            ?: throw TestHandlerNotFoundException()
    }
}
