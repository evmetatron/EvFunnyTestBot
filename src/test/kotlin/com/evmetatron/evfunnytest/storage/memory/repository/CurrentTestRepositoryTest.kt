/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.memory.repository

import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

internal class CurrentTestRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var currentTestRepository: CurrentTestRepository

    @Test
    fun `success write, read, delete`() {
        val test = createCurrentTestEntity()

        currentTestRepository.findByIdOrNull(test.userId) shouldBe null

        currentTestRepository.save(test)

        currentTestRepository.findByIdOrNull(test.userId) shouldBe test

        currentTestRepository.delete(test)

        currentTestRepository.findByIdOrNull(test.userId) shouldBe null
    }
}
