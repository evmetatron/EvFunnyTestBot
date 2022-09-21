/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.service

import com.evmetatron.evfunnytest.fixtures.createTestReplaceViewEntity
import com.evmetatron.evfunnytest.storage.db.repository.TestReplaceViewRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

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

        every { testReplaceViewRepository.findByIdOrNull(testId) } returns expected

        testReplaceService.getTest(testId) shouldBe expected
    }
}
