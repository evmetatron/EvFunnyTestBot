/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.memory.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("remove_buttons", timeToLive = 72000)
data class RemoveButtonsEntity(
    @Id
    val userId: Long,
    val chatId: Long,
    val messageIds: List<Int>,
) {
    fun withMessageId(messageId: Int): RemoveButtonsEntity =
        this.copy(messageIds = this.messageIds + messageId)
}
