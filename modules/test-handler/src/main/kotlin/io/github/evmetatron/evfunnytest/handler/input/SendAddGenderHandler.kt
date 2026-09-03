package io.github.evmetatron.evfunnytest.handler.input

import io.github.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.InputAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import io.github.evmetatron.evfunnytest.dto.button.GenderButton
import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.enumerable.Gender
import io.github.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Suppress("MagicNumber")
@Order(7)
@Component
class SendAddGenderHandler : AbstractInputHandler() {
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
        val addedText = when {
            context.isHandledStart() -> START_TEST_TEXT
            inputAdapter.button?.type == ButtonType.CANCEL_ANSWER -> CANCEL_ANSWER_TEXT
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
