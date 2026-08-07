package io.github.evmetatron.evfunnytest.dto.button

import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.exception.ConvertToDataException

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
