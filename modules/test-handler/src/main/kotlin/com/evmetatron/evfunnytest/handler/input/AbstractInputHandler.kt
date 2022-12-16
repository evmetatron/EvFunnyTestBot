/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

abstract class AbstractInputHandler(
    protected val inputHandler: InputHandler?,
) : InputHandler {
    override fun getObject(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter? =
        verify(inputAdapter, currentTestEntity, context).takeIf { it }
            ?.let { handle(inputAdapter, currentTestEntity, context) }
            ?: inputHandler?.getObject(inputAdapter, currentTestEntity, context)

    protected abstract fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean

    protected abstract fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter
}
