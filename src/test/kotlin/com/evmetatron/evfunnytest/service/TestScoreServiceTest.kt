/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.service

import com.evmetatron.evfunnytest.fixtures.createTestScoreViewEntity
import com.evmetatron.evfunnytest.storage.db.repository.TestScoreViewRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

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

        every { testScoreViewRepository.findByIdOrNull(testId) } returns expected

        testScoreService.getTest(testId) shouldBe expected
    }
}
