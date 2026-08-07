package io.github.evmetatron.evfunnytest.dto.button

import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.enumerable.Gender
import io.github.evmetatron.evfunnytest.exception.ConvertToDataException

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
