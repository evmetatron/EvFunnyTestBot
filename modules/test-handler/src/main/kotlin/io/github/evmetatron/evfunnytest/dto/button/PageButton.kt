package io.github.evmetatron.evfunnytest.dto.button

import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.exception.ConvertToDataException

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
