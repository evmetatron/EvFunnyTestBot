/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.utils

import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User

fun Update.getUser(): User =
    message?.from
        ?: callbackQuery.from

fun Update.getChat() =
    message?.chat
        ?: callbackQuery.message.chat
