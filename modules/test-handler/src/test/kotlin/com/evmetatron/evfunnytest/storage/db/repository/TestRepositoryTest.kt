/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.repository

import fixtures.testEntity5
import fixtures.testEntity6
import fixtures.testEntity7
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = [TestRepository::class])
internal class TestRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var testRepository: TestRepository

    @Test
    fun `success findLimited`() {
        val limit = 3
        val offset = 4
        val expected = listOf(
            testEntity5(),
            testEntity6(),
            testEntity7(),
        )

        testRepository.findLimited(limit, offset) shouldBe expected
    }
}
