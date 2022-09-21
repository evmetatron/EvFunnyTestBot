/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.exception.CurrentTestNotFound
import com.evmetatron.evfunnytest.exception.TestHandlerNotFoundException
import com.evmetatron.evfunnytest.handler.test.TestHandler
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

class HandleTestHandler(
    private val testHandler: TestHandler?,
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean =
        currentTestEntity?.isNeedGender() == false

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter {
        if (currentTestEntity == null) {
            throw CurrentTestNotFound()
        }

        return testHandler?.getObject(inputAdapter, currentTestEntity, context)
            ?: throw TestHandlerNotFoundException()
    }
}
