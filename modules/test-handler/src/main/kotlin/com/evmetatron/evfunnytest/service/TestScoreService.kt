package com.evmetatron.evfunnytest.service

import com.evmetatron.evfunnytest.storage.db.entity.TestScoreViewEntity
import com.evmetatron.evfunnytest.storage.db.repository.TestScoreViewRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class TestScoreService(
    private val testScoreViewRepository: TestScoreViewRepository
) {
    fun getTest(testId: Long): TestScoreViewEntity? =
        testScoreViewRepository.findByIdOrNull(testId)
}
