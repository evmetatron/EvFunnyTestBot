package io.github.evmetatron.evfunnytest.handler.input

import io.github.evmetatron.evfunnytest.dto.adapter.InputAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import io.github.evmetatron.evfunnytest.dto.button.GenderButton
import io.github.evmetatron.evfunnytest.dto.button.toConcreteButtonAs
import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.exception.CurrentTestNotFound
import io.github.evmetatron.evfunnytest.exception.InputHandlerNotFoundException
import io.github.evmetatron.evfunnytest.exception.TestHandlerNotFoundException
import io.github.evmetatron.evfunnytest.service.CurrentTestService
import io.github.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Suppress("MagicNumber")
@Order(6)
@Component
class GenderClickHandler(
    private val currentTestService: CurrentTestService,
) : AbstractInputHandler() {
    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean {
        val isCurrentTestExists = currentTestEntity != null
        val isGenderButton = inputAdapter.button?.type == ButtonType.SELECT_GENDER

        return isCurrentTestExists && isGenderButton
    }

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter {
        val genderButton = inputAdapter.button.toConcreteButtonAs<GenderButton>()

        if (currentTestEntity == null) {
            throw CurrentTestNotFound()
        }

        if (inputHandler == null) {
            throw TestHandlerNotFoundException()
        }

        val newCurrentTest = currentTestEntity.withGender(genderButton.gender)

        currentTestService.replaceCurrentTest(newCurrentTest)

        return inputHandler.getObject(inputAdapter, newCurrentTest, context)
            ?: throw InputHandlerNotFoundException()
    }
}
