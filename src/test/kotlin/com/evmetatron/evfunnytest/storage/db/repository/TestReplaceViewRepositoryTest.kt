/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.repository

import com.evmetatron.evfunnytest.fixtures.testReplaceViewEntity1
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

internal class TestReplaceViewRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var testReplaceViewRepository: TestReplaceViewRepository

    @Test
    fun `success findByIdOrNull`() {
        val expected = testReplaceViewEntity1()

        testReplaceViewRepository.findByIdOrNull(expected.id) shouldBe expected
    }
}
