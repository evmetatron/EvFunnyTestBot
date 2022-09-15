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

data class PageClick(
    val offset: Int,
) {
    companion object {
        fun ofButtonClick(buttonClick: ButtonClick): PageClick =
            (
                buttonClick.type == ButtonType.PAGE &&
                    buttonClick.data[ButtonClick.OFFSET] != null
                )
                .takeIf { it }
                ?.let {
                    PageClick(
                        offset = buttonClick.data[ButtonClick.OFFSET]!!.toInt(),
                    )
                }
                ?: throw ConvertToDataException("PageClick", buttonClick)
    }

    fun toButtonClick(): ButtonClick =
        ButtonClick(
            type = ButtonType.PAGE,
            data = mapOf(
                ButtonClick.OFFSET to offset.toString(),
            ),
        )
}
