/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.button

import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.exception.ConvertToDataException

data class TestVariableButton(
    val variableId: Long,
) : ConcreteButton {
    companion object {
        private const val VARIABLE_ID = "variableId"

        fun ofMap(map: Map<String, String>): TestVariableButton =
            map[VARIABLE_ID]
                ?.let {
                    TestVariableButton(
                        variableId = map[VARIABLE_ID]!!.toLong(),
                    )
                }
                ?: throw ConvertToDataException(TestVariableButton::class.simpleName!!, map)
    }

    override fun toBaseButton(): BaseButton =
        BaseButton(
            type = ButtonType.TEST_VARIABLE,
            data = mapOf(
                VARIABLE_ID to variableId.toString(),
            ),
        )
}
