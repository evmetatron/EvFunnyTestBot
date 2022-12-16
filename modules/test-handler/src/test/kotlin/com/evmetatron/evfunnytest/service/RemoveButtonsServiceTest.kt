/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.service

import fixtures.createRemoveButtonsEntity
import com.evmetatron.evfunnytest.storage.memory.repository.CurrentTestRepository
import com.evmetatron.evfunnytest.storage.memory.repository.RemoveButtonsRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(MockKExtension::class)
internal class RemoveButtonsServiceTest {
    @MockK
    private lateinit var currentTestRepository: CurrentTestRepository

    @MockK(relaxed = true)
    private lateinit var removeButtonsRepository: RemoveButtonsRepository

    @InjectMockKs
    private lateinit var removeButtonsService: RemoveButtonsService

    @Test
    fun `success findExpired`() {
        val removeButtons1 = createRemoveButtonsEntity(userId = 1)
        val removeButtons2 = createRemoveButtonsEntity(userId = 2)
        val removeButtons3 = createRemoveButtonsEntity(userId = 3)
        val removeButtons4 = createRemoveButtonsEntity(userId = 4)

        every { removeButtonsRepository.findAll() } returns listOf(
            removeButtons1,
            removeButtons2,
            removeButtons3,
            removeButtons4,
        )

        every {
            currentTestRepository.existsById(or(1, 4))
        } returns true

        every {
            currentTestRepository.existsById(or(2, 3))
        } returns false

        runBlocking {
            removeButtonsService.findExpired()
        } shouldBe listOf(
            removeButtons2,
            removeButtons3,
        )
    }

    @Test
    fun `success registerMessage - remove buttons exists`() {
        val userId = 1L
        val chatId = 10L
        val messageId = 156
        val removeButtons = createRemoveButtonsEntity(userId = userId)
        val expected = removeButtons.withMessageId(messageId)

        every { removeButtonsRepository.findByIdOrNull(userId) } returns removeButtons
        every { removeButtonsRepository.save(expected) } returns expected

        removeButtonsService.registerMessage(userId, chatId, messageId)

        verify(exactly = 1) { removeButtonsRepository.save(expected) }
    }

    @Test
    fun `success registerMessage - remove buttons not exists`() {
        val userId = 1L
        val chatId = 10L
        val messageId = 156
        val expected = createRemoveButtonsEntity(
            userId = userId,
            chatId = chatId,
            messageIds = listOf(messageId),
        )

        every { removeButtonsRepository.findByIdOrNull(userId) } returns null
        every { removeButtonsRepository.save(expected) } returns expected

        removeButtonsService.registerMessage(userId, chatId, messageId)

        verify(exactly = 1) { removeButtonsRepository.save(expected) }
    }

    @Test
    fun `success remove`() {
        val userId = 5L

        removeButtonsService.remove(userId)

        verify(exactly = 1) { removeButtonsRepository.deleteById(userId) }
    }

    @Test
    fun `success getByUserId`() {
        val userId = 5L
        val expected = createRemoveButtonsEntity(userId = userId)

        every { removeButtonsRepository.findByIdOrNull(userId) } returns expected

        removeButtonsService.getByUserId(userId) shouldBe expected
    }
}
