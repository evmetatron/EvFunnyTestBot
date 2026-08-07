package com.evmetatron.evfunnytest.dto.adapter

sealed interface MessageAdapter {
    val chatId: Long
    val clearButtonsLater: Boolean
}
