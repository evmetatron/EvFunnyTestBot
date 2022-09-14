/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.repository

import com.evmetatron.evfunnytest.fixtures.testEntity5
import com.evmetatron.evfunnytest.fixtures.testEntity6
import com.evmetatron.evfunnytest.fixtures.testEntity7
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

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
