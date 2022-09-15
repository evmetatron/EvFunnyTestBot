/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.test

import com.evmetatron.evfunnytest.enumerable.TestType
import org.springframework.context.ApplicationEventPublisher

class ReplaceTestHandler(
    publisher: ApplicationEventPublisher,
    testHandler: TestHandler?
) : AbstractTestHandler(publisher, testHandler) {
    override fun testType(): TestType {
        TODO("Not yet implemented")
    }
}
