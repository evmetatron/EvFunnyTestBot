package io.github.evmetatron.evfunnytest.service

import io.github.evmetatron.evfunnytest.storage.db.entity.TestReplaceViewEntity
import io.github.evmetatron.evfunnytest.storage.db.repository.TestReplaceViewRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class TestReplaceService(
    private val testReplaceViewRepository: TestReplaceViewRepository
) {
    fun getTest(testId: Long): TestReplaceViewEntity? =
        testReplaceViewRepository.findByIdOrNull(testId)
}
