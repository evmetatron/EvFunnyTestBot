/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.repository

import fixtures.testScoreViewEntity9
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

internal class TestScoreViewRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var testScoreViewRepository: TestScoreViewRepository

    @Test
    fun `success findByIdOrNull`() {
        val expected = testScoreViewEntity9()

        testScoreViewRepository.findByIdOrNull(expected.id) shouldBe expected
    }
}
