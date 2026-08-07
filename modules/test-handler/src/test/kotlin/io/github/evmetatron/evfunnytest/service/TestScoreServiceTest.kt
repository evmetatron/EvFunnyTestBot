package io.github.evmetatron.evfunnytest.service

import fixtures.createTestScoreViewEntity
import io.github.evmetatron.evfunnytest.storage.db.repository.TestScoreViewRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional

@ExtendWith(MockKExtension::class)
class TestScoreServiceTest {
    @MockK
    private lateinit var testScoreViewRepository: TestScoreViewRepository

    @InjectMockKs
    private lateinit var testScoreService: TestScoreService

    @Test
    fun `success getTest`() {
        val testId = 46L
        val expected = createTestScoreViewEntity()

        every { testScoreViewRepository.findById(testId) } returns Optional.ofNullable(expected)

        testScoreService.getTest(testId) shouldBe expected
    }
}
