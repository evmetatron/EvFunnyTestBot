package io.github.evmetatron.evfunnytest.dto.adapter

import io.github.evmetatron.evfunnytest.dto.button.BaseButton
import io.github.evmetatron.evfunnytest.enumerable.BotCommand

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
