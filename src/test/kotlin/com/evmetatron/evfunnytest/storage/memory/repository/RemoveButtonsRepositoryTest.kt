/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.memory.repository

import com.evmetatron.evfunnytest.fixtures.createRemoveButtonsEntity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

internal class RemoveButtonsRepositoryTest : BaseTest() {
    @Autowired
    private lateinit var removeButtonsRepository: RemoveButtonsRepository

    @Test
    fun `success write, read, delete`() {
        val removeButtons = createRemoveButtonsEntity()

        removeButtonsRepository.findByIdOrNull(removeButtons.userId) shouldBe null

        removeButtonsRepository.save(removeButtons)

        removeButtonsRepository.findByIdOrNull(removeButtons.userId) shouldBe removeButtons

        removeButtonsRepository.delete(removeButtons)

        removeButtonsRepository.findByIdOrNull(removeButtons.userId) shouldBe null
    }
}
