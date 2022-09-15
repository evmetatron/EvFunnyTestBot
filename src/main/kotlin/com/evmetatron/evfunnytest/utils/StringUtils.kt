/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.utils

fun String.withText(text: String?): String =
    text?.let {
        "$text\n\n======================\n\n$this"
    } ?: this
