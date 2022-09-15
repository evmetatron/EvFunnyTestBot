/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.GetTestClick
import com.evmetatron.evfunnytest.dto.StartTestClick
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.storage.db.repository.TestRepository
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.utils.getButtonClick
import com.evmetatron.evfunnytest.utils.getChat
import com.evmetatron.evfunnytest.utils.toSendMessage
import org.springframework.data.repository.findByIdOrNull
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.EntityType
import org.telegram.telegrambots.meta.api.objects.MessageEntity
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

class GetTestClickHandler(
    private val testRepository: TestRepository,
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
    companion object {
        const val BUTTON_TEXT = "Начать тест"
        const val TEST_NOT_FOUND = "Тест не найден"
    }

    override fun verify(update: Update, currentTestEntity: CurrentTestEntity?): Boolean {
        val isEmptyCurrentTest = currentTestEntity == null
        val isGetTest = update.getButtonClick()?.let { it.type == ButtonType.GET_TEST } ?: false

        return isEmptyCurrentTest && isGetTest
    }

    override fun handle(update: Update, currentTestEntity: CurrentTestEntity?): PartialBotApiMethod<*> {
        // Проверка на существование произведена в verify
        val getTestClick = update.getButtonClick()!!.let { GetTestClick.ofButtonClick(it) }

        val test = testRepository.findByIdOrNull(getTestClick.testId) ?: return update.toSendMessage(TEST_NOT_FOUND)

        val text = "${test.name}\n\n${test.description}"

        return SendMessage().apply {
            this.text = text
            this.chatId = update.getChat().id.toString()
            this.entities = listOf(
                MessageEntity().apply {
                    this.type = EntityType.BOLD
                    this.text = test.name
                    this.offset = 0
                    this.length = test.name.length
                },
                MessageEntity().apply {
                    this.type = EntityType.ITALIC
                    this.text = test.description
                    this.offset = text.length - test.description.length
                    this.length = test.description.length
                },
            )
            this.replyMarkup = InlineKeyboardMarkup().apply {
                this.keyboard = listOf(
                    listOf(
                        InlineKeyboardButton().apply {
                            this.text = BUTTON_TEXT
                            this.callbackData = StartTestClick(
                                testId = test.id,
                            ).toButtonClick().toJson()
                        }
                    ),
                )
            }
        }
    }
}
