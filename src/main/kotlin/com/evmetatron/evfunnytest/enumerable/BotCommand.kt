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

    fun getCommandByInput(input: String): BotCommand? =
        TODO(input)
}
