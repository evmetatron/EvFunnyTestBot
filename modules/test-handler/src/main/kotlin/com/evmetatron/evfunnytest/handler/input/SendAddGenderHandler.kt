/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import com.evmetatron.evfunnytest.dto.button.GenderButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.enumerable.Gender
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

class SendAddGenderHandler(
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
    companion object {
        const val START_TEST_TEXT = "Запущен тест"
        const val CANCEL_ANSWER_TEXT = "Ответ отменен"
        const val ERROR_MESSAGE_TEXT = "Необходимо выбрать пол по кнопке или завершить тест"
        const val SELECT_GENDER_TEXT = "Выберите пол"
        const val MALE_GENDER_TEXT = "Мужской"
        const val FEMALE_GENDER_TEXT = "Женский"
    }
    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean =
        currentTestEntity?.isNeedGender() ?: false

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter {
        val addedText = when (true) {
            context.isHandledStart() -> START_TEST_TEXT
            (inputAdapter.button?.type == ButtonType.CANCEL_ANSWER) -> CANCEL_ANSWER_TEXT
            else -> ERROR_MESSAGE_TEXT
        }

        return SendMessageAdapter(
            clearButtonsLater = true,
            chatId = inputAdapter.chatId,
            text = "[u]$addedText[/u]\n\n$SELECT_GENDER_TEXT",
            buttons = listOf(
                listOf(
                    ButtonAdapter(
                        text = MALE_GENDER_TEXT,
                        button = GenderButton(gender = Gender.MALE).toBaseButton(),
                    ),
                    ButtonAdapter(
                        text = FEMALE_GENDER_TEXT,
                        button = GenderButton(gender = Gender.FEMALE).toBaseButton(),
                    ),
                ),
                listOf(
                    ButtonAdapter.createExitTestButton(),
                ),
            ),
        )
    }
}
