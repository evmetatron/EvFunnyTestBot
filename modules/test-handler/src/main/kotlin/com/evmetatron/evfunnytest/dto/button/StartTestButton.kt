/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.button

import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.exception.ConvertToDataException

data class StartTestButton(
    val testId: Long,
) : ConcreteButton {
    companion object {
        private const val TEST_ID = "testId"

        fun ofMap(map: Map<String, String>): StartTestButton =
            map[TEST_ID]
                ?.let {
                    StartTestButton(
                        testId = map[TEST_ID]!!.toLong(),
                    )
                }
                ?: throw ConvertToDataException(StartTestButton::class.simpleName!!, map)
    }

    override fun toBaseButton(): BaseButton =
        BaseButton(
            type = ButtonType.START_TEST,
            data = mapOf(
                TEST_ID to testId.toString(),
            ),
        )
}
