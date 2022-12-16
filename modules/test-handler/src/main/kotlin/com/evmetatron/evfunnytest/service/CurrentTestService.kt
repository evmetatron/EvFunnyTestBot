/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.service

import com.evmetatron.evfunnytest.storage.db.entity.TestEntity
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.storage.memory.repository.CurrentTestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class CurrentTestService(
    private val currentTestRepository: CurrentTestRepository,
) {
    fun getCurrentTest(userId: Long): CurrentTestEntity? =
        currentTestRepository.findByIdOrNull(userId)

    fun createCurrentTest(userId: Long, testEntity: TestEntity): CurrentTestEntity =
        CurrentTestEntity(
            userId = userId,
            testId = testEntity.id,
            type = testEntity.type,
            allowGender = testEntity.allowGender,
        ).apply { currentTestRepository.save(this) }

    fun removeCurrentTest(userId: Long) =
        currentTestRepository.deleteById(userId)

    fun replaceCurrentTest(currentTestEntity: CurrentTestEntity) {
        currentTestRepository.save(currentTestEntity)
    }
}
