/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.adapter

data class EditButtonsAdapter(
    override val chatId: Long,
    override val clearButtonsLater: Boolean = false,
    val messageId: Int,
    val buttons: List<List<ButtonAdapter>>? = null,
) : MessageAdapter
