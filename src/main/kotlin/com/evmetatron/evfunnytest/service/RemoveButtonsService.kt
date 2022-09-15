/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.service

import com.evmetatron.evfunnytest.storage.memory.entity.RemoveButtonsEntity
import com.evmetatron.evfunnytest.storage.memory.repository.CurrentTestRepository
import com.evmetatron.evfunnytest.storage.memory.repository.RemoveButtonsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class RemoveButtonsService(
    private val currentTestRepository: CurrentTestRepository,
    private val removeButtonsRepository: RemoveButtonsRepository,
) {
    suspend fun findExpired(): List<RemoveButtonsEntity> = coroutineScope {
        withContext(Dispatchers.IO) { removeButtonsRepository.findAll() }
            .map { removeButtons ->
                async(Dispatchers.IO) {
                    currentTestRepository.existsById(removeButtons.userId)
                        .takeUnless { it }
                        ?.let { removeButtons }
                }
            }
            .mapNotNull { it.await() }
    }

    fun registerMessage(userId: Long, chatId: Long, messageId: Int) {
        val removeButtons = removeButtonsRepository.findByIdOrNull(userId)?.withMessageId(messageId)
            ?: RemoveButtonsEntity(
                userId = userId,
                chatId = chatId,
                messageIds = listOf(messageId),
            )

        removeButtonsRepository.save(removeButtons)
    }

    fun remove(userId: Long) {
        removeButtonsRepository.deleteById(userId)
    }

    fun getByUserId(userId: Long): RemoveButtonsEntity? =
        removeButtonsRepository.findByIdOrNull(userId)
}
