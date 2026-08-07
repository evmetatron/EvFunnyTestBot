package io.github.evmetatron.evfunnytest.service

import fixtures.createTestReplaceViewEntity
import io.github.evmetatron.evfunnytest.storage.db.repository.TestReplaceViewRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import fixtures.asOptional

@ExtendWith(MockKExtension::class)
class TestReplaceServiceTest {
    @MockK
    private lateinit var testReplaceViewRepository: TestReplaceViewRepository

    @InjectMockKs
    private lateinit var testReplaceService: TestReplaceService

    @Test
    fun `success getTest`() {
        val testId = 46L
        val expected = createTestReplaceViewEntity()

        every { testReplaceViewRepository.findById(testId) } returns expected.asOptional()

        testReplaceService.getTest(testId) shouldBe expected
    }
}
