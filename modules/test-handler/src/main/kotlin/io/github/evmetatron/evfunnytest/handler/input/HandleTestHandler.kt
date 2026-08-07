package io.github.evmetatron.evfunnytest.handler.input

import io.github.evmetatron.evfunnytest.dto.adapter.InputAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.exception.CurrentTestNotFound
import io.github.evmetatron.evfunnytest.exception.TestHandlerNotFoundException
import io.github.evmetatron.evfunnytest.handler.test.TestHandler
import io.github.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Suppress("MagicNumber")
@Order(8)
@Component
class HandleTestHandler(
    private val testHandler: TestHandler?,
) : AbstractInputHandler() {
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
