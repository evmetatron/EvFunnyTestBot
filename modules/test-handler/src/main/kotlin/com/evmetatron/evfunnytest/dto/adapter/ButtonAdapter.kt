/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.adapter

import com.evmetatron.evfunnytest.dto.button.BaseButton
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.utils.MESSAGE_BOT_BUTTON_CANCEL_ANSWER
import com.evmetatron.evfunnytest.utils.MESSAGE_EXIT_TEST_BUTTON_TEXT

data class ButtonAdapter(
    val text: String,
    val button: BaseButton,
) {
    companion object {
        fun createCancelAnswerButton(): ButtonAdapter =
            ButtonAdapter(
                text = MESSAGE_BOT_BUTTON_CANCEL_ANSWER,
                button = BaseButton(type = ButtonType.CANCEL_ANSWER),
            )

        fun createExitTestButton(): ButtonAdapter =
            ButtonAdapter(
                text = MESSAGE_EXIT_TEST_BUTTON_TEXT,
                button = BaseButton(type = ButtonType.EXIT_TEST),
            )
    }
}
