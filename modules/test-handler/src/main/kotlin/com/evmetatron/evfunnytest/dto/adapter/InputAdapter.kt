/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.adapter

import com.evmetatron.evfunnytest.dto.button.BaseButton
import com.evmetatron.evfunnytest.enumerable.BotCommand

data class InputAdapter(
    val chatId: Long,
    val messageId: Int,
    val text: String?,
    val user: UserAdapter,
    val button: BaseButton?,
    val command: BotCommand?,
) {
    fun toSendMessage(text: String): SendMessageAdapter =
        SendMessageAdapter(
            chatId = chatId,
            text = text,
        )

    fun isMessageOnly(): Boolean =
        this.button == null && this.command == null && this.text != null
}
