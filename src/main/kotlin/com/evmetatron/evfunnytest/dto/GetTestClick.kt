/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto

import com.evmetatron.evfunnytest.enumerable.ButtonType

data class GetTestClick(
    val testId: Long,
) {
    fun toButtonClick(): ButtonClick =
        ButtonClick(
            type = ButtonType.GET_TEST,
            data = mapOf(
                ButtonClick.TEST_ID to testId.toString(),
            ),
        )
}
