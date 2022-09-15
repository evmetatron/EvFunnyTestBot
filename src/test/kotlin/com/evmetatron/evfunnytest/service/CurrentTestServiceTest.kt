/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.service

import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import com.evmetatron.evfunnytest.fixtures.createTestEntity
import com.evmetatron.evfunnytest.storage.memory.repository.CurrentTestRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(MockKExtension::class)
internal class CurrentTestServiceTest {
    @MockK
    private lateinit var currentTestRepository: CurrentTestRepository

    @InjectMockKs
    private lateinit var currentTestService: CurrentTestService

    @Test
    fun `success getCurrentTest`() {
        val userId = 1L
        val expected = createCurrentTestEntity(userId = userId)

        every { currentTestRepository.findByIdOrNull(userId) } returns expected

        currentTestService.getCurrentTest(userId) shouldBe expected
    }

    @Test
    fun `success createCurrentTest`() {
        val userId = 1L
        val test = createTestEntity()
        val expected = createCurrentTestEntity(
            userId = userId,
            testId = test.id,
            type = test.type,
            gender = null,
            allowGender = test.allowGender,
            answers = emptyList(),
        )

        every { currentTestRepository.save(expected) } returns expected

        currentTestService.createCurrentTest(userId, test) shouldBe expected

        verify(exactly = 1) { currentTestRepository.save(expected) }
    }
}
