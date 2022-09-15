/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.button

import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.exception.ConvertToDataException

data class GetTestButton(
    val testId: Long,
) : ConcreteButton {
    companion object {
        private const val TEST_ID = "testId"

        fun ofMap(map: Map<String, String>): GetTestButton =
            map[TEST_ID]
                ?.let {
                    GetTestButton(
                        testId = map[TEST_ID]!!.toLong(),
                    )
                }
                ?: throw ConvertToDataException(GetTestButton::class.simpleName!!, map)
    }

    override fun toBaseButton(): BaseButton =
        BaseButton(
            type = ButtonType.GET_TEST,
            data = mapOf(
                TEST_ID to testId.toString(),
            ),
        )
}
