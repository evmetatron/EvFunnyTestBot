/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.service

import fixtures.createTestEntity
import com.evmetatron.evfunnytest.storage.db.repository.TestRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(MockKExtension::class)
internal class TestServiceTest {
    @MockK
    private lateinit var testRepository: TestRepository

    @InjectMockKs
    private lateinit var testService: TestService

    @Test
    fun `success findTests`() {
        val limit = 15
        val offset = 15

        val expected = (1..15).map { createTestEntity() }

        every { testRepository.findLimited(limit, offset) } returns expected

        testService.findTests(limit, offset) shouldBe expected
    }

    @Test
    fun `success getTest`() {
        val testId = 17L

        val expected = createTestEntity()

        every { testRepository.findByIdOrNull(testId) } returns expected

        testService.getTest(testId) shouldBe expected
    }
}
