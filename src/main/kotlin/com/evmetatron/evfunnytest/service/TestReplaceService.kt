/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.service

import com.evmetatron.evfunnytest.storage.db.entity.TestReplaceViewEntity
import com.evmetatron.evfunnytest.storage.db.repository.TestReplaceViewRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class TestReplaceService(
    private val testReplaceViewRepository: TestReplaceViewRepository
) {
    fun getTest(testId: Long): TestReplaceViewEntity? =
        testReplaceViewRepository.findByIdOrNull(testId)
}
