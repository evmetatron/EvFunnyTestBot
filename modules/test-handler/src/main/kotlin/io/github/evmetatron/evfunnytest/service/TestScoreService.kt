package io.github.evmetatron.evfunnytest.service

import io.github.evmetatron.evfunnytest.storage.db.entity.TestScoreViewEntity
import io.github.evmetatron.evfunnytest.storage.db.repository.TestScoreViewRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class TestScoreService(
    private val testScoreViewRepository: TestScoreViewRepository
) {
    fun getTest(testId: Long): TestScoreViewEntity? =
        testScoreViewRepository.findByIdOrNull(testId)
}
