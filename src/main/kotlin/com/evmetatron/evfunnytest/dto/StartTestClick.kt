/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto

import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.exception.ConvertToDataException

data class StartTestClick(
    val testId: Long,
) {
    companion object {
        fun ofButtonClick(buttonClick: ButtonClick): StartTestClick =
            (
                buttonClick.type == ButtonType.START_TEST &&
                    buttonClick.data[ButtonClick.TEST_ID] != null
                )
                .takeIf { it }
                ?.let {
                    StartTestClick(
                        testId = buttonClick.data[ButtonClick.TEST_ID]!!.toLong(),
                    )
                }
                ?: throw ConvertToDataException("StartTestClick", buttonClick)
    }

    fun toButtonClick(): ButtonClick =
        ButtonClick(
            type = ButtonType.START_TEST,
            data = mapOf(
                ButtonClick.TEST_ID to testId.toString(),
            ),
        )
}
