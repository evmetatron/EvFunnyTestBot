/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.test

import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

abstract class AbstractTestHandler(
    private val testHandler: TestHandler?,
) : TestHandler {
    override fun getObject(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity,
        context: HandlerContext,
    ): MessageAdapter? {
        if (currentTestEntity.type != testType()) {
            return testHandler?.getObject(inputAdapter, currentTestEntity, context)
        }

        return handle(inputAdapter, currentTestEntity, context)
    }

    protected abstract fun testType(): TestType

    protected abstract fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity,
        context: HandlerContext,
    ): MessageAdapter
}
