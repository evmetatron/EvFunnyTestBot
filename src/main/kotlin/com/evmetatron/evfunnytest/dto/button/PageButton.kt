/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.button

import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.exception.ConvertToDataException

data class PageButton(
    val offset: Int,
) : ConcreteButton {
    companion object {
        private const val OFFSET = "offset"

        fun ofMap(map: Map<String, String>): PageButton =
            map[OFFSET]
                ?.let {
                    PageButton(
                        offset = map[OFFSET]!!.toInt(),
                    )
                }
                ?: throw ConvertToDataException(PageButton::class.simpleName!!, map)
    }

    override fun toBaseButton(): BaseButton =
        BaseButton(
            type = ButtonType.PAGE,
            data = mapOf(
                OFFSET to offset.toString(),
            ),
        )
}
