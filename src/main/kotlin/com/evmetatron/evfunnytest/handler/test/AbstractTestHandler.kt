/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.test

import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.springframework.context.ApplicationEventPublisher

abstract class AbstractTestHandler(
    private val publisher: ApplicationEventPublisher,
    private val testHandler: TestHandler?,
) : TestHandler {
    override fun getObject(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity,
        text: String?
    ): MessageAdapter? {
        if (currentTestEntity.type != testType()) {
            return testHandler?.getObject(inputAdapter, currentTestEntity, text)
        }

        return null
    }

    protected abstract fun testType(): TestType
}
