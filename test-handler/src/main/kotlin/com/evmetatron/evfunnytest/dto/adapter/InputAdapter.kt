/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.adapter

import com.evmetatron.evfunnytest.dto.adapter.textselection.DefaultSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.TextSelection
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
    fun toSendMessage(vararg text: TextSelection): SendMessageAdapter =
        toSendMessage(text.toList())

    fun toSendMessage(text: List<TextSelection>): SendMessageAdapter =
        SendMessageAdapter(
            chatId = chatId,
            text = text,
        )

    fun toSendMessageDefault(text: String): SendMessageAdapter =
        toSendMessage(
            DefaultSelection(text),
        )

    fun isMessageOnly(): Boolean =
        this.button == null && this.command == null && this.text != null
}
