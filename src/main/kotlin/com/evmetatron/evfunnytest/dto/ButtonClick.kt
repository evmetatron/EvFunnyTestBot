/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto

import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.google.gson.Gson

data class ButtonClick(
    val type: ButtonType,
    val data: Map<String, String>,
) {
    companion object {
        const val OFFSET = "offset"
        const val TEST_ID = "testId"
    }

    fun toJson(): String =
        Gson().toJson(this)
}
