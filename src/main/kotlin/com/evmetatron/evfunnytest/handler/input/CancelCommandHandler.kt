/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.data.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.data.memory.repository.CurrentTestRepository
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.utils.getChat
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

class CancelCommandHandler(
    private val currentTestRepository: CurrentTestRepository,
    private val inputHandler: InputHandler?,
) : InputHandler {
    override fun execute(
        update: Update,
        currentTestEntity: CurrentTestEntity?,
        botCommand: BotCommand?,
    ): PartialBotApiMethod<*> {
        return SendMessage().apply {
            this.chatId = update.getChat().id.toString()
            this.text = "TEST"
            this.replyMarkup = InlineKeyboardMarkup().apply {
                this.keyboard = listOf(
                    listOf(
                        InlineKeyboardButton().apply {
                            this.text = "Test"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test 2"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test 3"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test 2"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test 3"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test 2"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test 3"
                            this.callbackData = "/test 111"
                        },
                    ),
                    listOf(
                        InlineKeyboardButton().apply {
                            this.text = "Test 4"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test 5"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test 6"
                            this.callbackData = "/test 111"
                        },
                    ),
                    listOf(
                        InlineKeyboardButton().apply {
                            this.text = "Test 7"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test 8"
                            this.callbackData = "/test 111"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "Test 9"
                            this.callbackData = "/test 111"
                        },
                    ),
                )
            }
        }
    }
}
