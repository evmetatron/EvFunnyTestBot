/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.adapter

import com.evmetatron.evfunnytest.dto.adapter.textselection.TextSelection

data class SendMessageAdapter(
    override val chatId: Long,
    override val clearButtonsLater: Boolean = false,
    val text: List<TextSelection>? = null,
    val buttons: List<List<ButtonAdapter>>? = null,
) : MessageAdapter
