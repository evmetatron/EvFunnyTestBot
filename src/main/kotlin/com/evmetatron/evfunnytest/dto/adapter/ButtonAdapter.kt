/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.adapter

import com.evmetatron.evfunnytest.dto.button.BaseButton
import com.evmetatron.evfunnytest.enumerable.ButtonType

data class ButtonAdapter(
    val text: String,
    val button: BaseButton,
) {
    companion object {
        const val CANCEL_ANSWER_BUTTON_TEXT = "Отменить предыдущий ответ"
        const val EXIT_TEST_BUTTON_TEXT = "Выйти из теста"

        fun createCancelAnswerButton(): ButtonAdapter =
            ButtonAdapter(
                text = CANCEL_ANSWER_BUTTON_TEXT,
                button = BaseButton(type = ButtonType.CANCEL_ANSWER),
            )

        fun createExitTestButton(): ButtonAdapter =
            ButtonAdapter(
                text = EXIT_TEST_BUTTON_TEXT,
                button = BaseButton(type = ButtonType.EXIT_TEST),
            )
    }
}
