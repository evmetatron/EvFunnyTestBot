/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.button

import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.exception.ConvertToDataException

data class GetTestClick(
    val testId: Long,
) {
    companion object {
        fun ofButtonClick(buttonClick: ButtonClick): GetTestClick =
            (
                buttonClick.type == ButtonType.GET_TEST &&
                    buttonClick.data[ButtonClick.TEST_ID] != null
                )
                .takeIf { it }
                ?.let {
                    GetTestClick(
                        testId = buttonClick.data[ButtonClick.TEST_ID]!!.toLong(),
                    )
                }
                ?: throw ConvertToDataException("GetTestClick", buttonClick)
    }

    fun toButtonClick(): ButtonClick =
        ButtonClick(
            type = ButtonType.GET_TEST,
            data = mapOf(
                ButtonClick.TEST_ID to testId.toString(),
            ),
        )
}
