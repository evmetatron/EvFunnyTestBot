package io.github.evmetatron.evfunnytest.handler.input

import io.github.evmetatron.evfunnytest.dto.adapter.InputAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import io.github.evmetatron.spring.cor.ChainNext

abstract class AbstractInputHandler : InputHandler {
    @ChainNext
    protected lateinit var inputHandler: InputHandler

    override fun getObject(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter? =
        verify(inputAdapter, currentTestEntity, context).takeIf { it }
            ?.let { handle(inputAdapter, currentTestEntity, context) }
            ?: inputHandler.getObject(inputAdapter, currentTestEntity, context)

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
