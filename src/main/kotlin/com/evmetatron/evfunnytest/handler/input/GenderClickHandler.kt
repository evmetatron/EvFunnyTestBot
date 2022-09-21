/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.button.GenderButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.exception.CurrentTestNotFound
import com.evmetatron.evfunnytest.exception.InputHandlerNotFoundException
import com.evmetatron.evfunnytest.exception.TestHandlerNotFoundException
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

class GenderClickHandler(
    private val currentTestService: CurrentTestService,
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
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
        val genderButton = inputAdapter.button?.toConcreteButton() as GenderButton

        if (currentTestEntity == null) {
            throw CurrentTestNotFound()
        }

        if (inputHandler == null) {
            throw TestHandlerNotFoundException()
        }

        currentTestService.replaceCurrentTest(currentTestEntity.withGender(genderButton.gender))

        return inputHandler.getObject(inputAdapter, currentTestEntity, context)
            ?: throw InputHandlerNotFoundException()
    }
}
