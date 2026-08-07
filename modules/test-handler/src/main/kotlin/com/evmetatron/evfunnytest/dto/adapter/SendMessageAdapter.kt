package com.evmetatron.evfunnytest.dto.adapter

data class SendMessageAdapter(
    override val chatId: Long,
    override val clearButtonsLater: Boolean = false,
    val text: String? = null,
    val buttons: List<List<ButtonAdapter>>? = null,
) : MessageAdapter
