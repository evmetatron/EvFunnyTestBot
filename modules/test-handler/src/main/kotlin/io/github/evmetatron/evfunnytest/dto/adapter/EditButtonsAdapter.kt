package io.github.evmetatron.evfunnytest.dto.adapter

data class EditButtonsAdapter(
    override val chatId: Long,
    override val clearButtonsLater: Boolean = false,
    val messageId: Int,
    val buttons: List<List<ButtonAdapter>>? = null,
) : MessageAdapter
