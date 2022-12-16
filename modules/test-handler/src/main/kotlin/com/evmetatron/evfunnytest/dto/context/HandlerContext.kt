/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.context

data class HandlerContext(
    val context: Map<String, Any> = emptyMap()
) {
    companion object {
        const val HANDLED_START = "handledStart"
    }

    fun withHandledStart(): HandlerContext =
        this.copy(context = context + (HANDLED_START to true))

    fun isHandledStart(): Boolean =
        this.context[HANDLED_START]?.let { it as Boolean } ?: false
}
