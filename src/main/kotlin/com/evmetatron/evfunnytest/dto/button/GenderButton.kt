/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.button

import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.enumerable.Gender
import com.evmetatron.evfunnytest.exception.ConvertToDataException

data class GenderButton(
    val gender: Gender,
) : ConcreteButton {
    companion object {
        private const val GENDER = "gender"
        fun ofMap(map: Map<String, String>): GenderButton =
            map[GENDER]
                ?.let {
                    GenderButton(
                        gender = Gender.valueOf(map[GENDER]!!),
                    )
                }
                ?: throw ConvertToDataException(GenderButton::class.simpleName!!, map)
    }

    override fun toBaseButton(): BaseButton =
        BaseButton(
            type = ButtonType.SELECT_GENDER,
            data = mapOf(
                GENDER to gender.toString(),
            ),
        )
}
