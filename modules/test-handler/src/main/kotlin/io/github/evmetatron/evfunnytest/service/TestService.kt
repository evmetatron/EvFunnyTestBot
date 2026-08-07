package io.github.evmetatron.evfunnytest.service

import io.github.evmetatron.evfunnytest.storage.db.entity.TestEntity
import io.github.evmetatron.evfunnytest.storage.db.repository.TestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class TestService(
    private val testRepository: TestRepository,
) {
    fun findTests(limit: Int, offset: Int): List<TestEntity> =
        testRepository.findLimited(limit, offset)

    fun getTest(testId: Long): TestEntity? =
        testRepository.findByIdOrNull(testId)
}
