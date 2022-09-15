/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.button.GetTestClick
import com.evmetatron.evfunnytest.dto.button.PageClick
import com.evmetatron.evfunnytest.storage.db.repository.TestRepository
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.utils.getBotCommand
import com.evmetatron.evfunnytest.utils.getButtonClick
import com.evmetatron.evfunnytest.utils.getChat
import com.evmetatron.evfunnytest.utils.getUser
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

class ListCommandHandler(
    private val testRepository: TestRepository,
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
    companion object {
        const val DEFAULT_LIMIT = 15
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_CHUNK = 3
        const val HELLO_TEXT = """
                Привет {user}
                
                Я бот с забавными тестами.
                
                Можешь выбрать любой тест и пройти его
            """
    }

    override fun verify(update: Update, currentTestEntity: CurrentTestEntity?): Boolean {
        val isEmptyCurrentTest = currentTestEntity == null
        val isListCommand = update.getBotCommand()?.let { it == BotCommand.START || it == BotCommand.LIST } ?: false
        val isClickPage = update.getButtonClick()?.let { it.type == ButtonType.PAGE } ?: false

        return isEmptyCurrentTest && (isListCommand || isClickPage)
    }

    override fun handle(update: Update, currentTestEntity: CurrentTestEntity?): PartialBotApiMethod<*> {
        val pageClick = update.getButtonClick()?.let { PageClick.ofButtonClick(it) }

        val offset = pageClick?.offset ?: DEFAULT_OFFSET

        val tests = testRepository.findLimited(DEFAULT_LIMIT + 1, offset)

        val hasNext = tests.size > DEFAULT_LIMIT

        val text = HELLO_TEXT
            .replace("{user}", "${update.getUser().firstName} ${update.getUser().lastName}")
            .trimIndent()

        val replyKeyboardMarkup = InlineKeyboardMarkup().apply {
            this.keyboard = tests.take(DEFAULT_LIMIT).chunked(DEFAULT_CHUNK)
                .map { chunkTests ->
                    chunkTests.map { test ->
                        InlineKeyboardButton().apply {
                            this.text = test.name
                            this.callbackData = GetTestClick(
                                testId = test.id,
                            ).toButtonClick().toJson()
                        }
                    }
                } + listOf(
                listOfNotNull(
                    offset.takeIf { it > 0 }
                        ?.let {
                            InlineKeyboardButton().apply {
                                this.text = "⏮"
                                this.callbackData = PageClick(
                                    offset = offset - DEFAULT_LIMIT,
                                ).toButtonClick().toJson()
                            }
                        },
                    hasNext.takeIf { it }
                        ?.let {
                            InlineKeyboardButton().apply {
                                this.text = "⏭"
                                this.callbackData = PageClick(
                                    offset = offset + DEFAULT_LIMIT,
                                ).toButtonClick().toJson()
                            }
                        },
                ),
            )
        }

        return if (pageClick == null) {
            SendMessage().apply {
                this.text = text
                this.chatId = update.getChat().id.toString()
                this.replyMarkup = replyKeyboardMarkup
            }
        } else {
            EditMessageReplyMarkup().apply {
                this.chatId = update.getChat().id.toString()
                this.messageId = update.callbackQuery.message.messageId
                this.replyMarkup = replyKeyboardMarkup
            }
        }
    }
}
