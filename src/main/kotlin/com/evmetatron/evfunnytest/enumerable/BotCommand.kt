/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.enumerable

enum class BotCommand {
    START,
    LIST,
    CANCEL,
    EXIT;

    companion object {
        fun getCommandByInput(input: String): BotCommand? =
            "^/(\\w+)$".toRegex().find(input)
                ?.groups
                ?.last()
                ?.value
                ?.uppercase()
                ?.takeIf { command ->
                    command in values().map { it.name }
                }
                ?.let { valueOf(it) }
    }
}
