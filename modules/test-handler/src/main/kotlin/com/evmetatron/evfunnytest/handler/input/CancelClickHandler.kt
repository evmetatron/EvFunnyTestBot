/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.exception.TestHandlerNotFoundException
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

class CancelClickHandler(
    private val currentTestService: CurrentTestService,
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
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

        return inputHandler?.getObject(inputAdapter, updatedCurrentTest, context)
            ?: throw TestHandlerNotFoundException()
    }
}
