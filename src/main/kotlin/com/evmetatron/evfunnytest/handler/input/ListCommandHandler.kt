/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.data.db.repository.TestViewRepository
import com.evmetatron.evfunnytest.data.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.utils.getChat
import com.evmetatron.evfunnytest.utils.getUser
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

class ListCommandHandler(
    private val testViewRepository: TestViewRepository,
    private val inputHandler: InputHandler?,
) : InputHandler {
    private companion object {
        private const val DEFAULT_LIMIT = 15
        private const val DEFAULT_OFFSET = 0
        private const val CHUNK = 3
    }

    override fun execute(
        update: Update,
        currentTestEntity: CurrentTestEntity?,
        botCommand: BotCommand?
    ): PartialBotApiMethod<*>? {
        if ((botCommand != BotCommand.START && botCommand != BotCommand.LIST) || currentTestEntity != null) {
            return inputHandler?.execute(update, currentTestEntity, botCommand)
        }

        val tests = testViewRepository.findLimited(DEFAULT_LIMIT, DEFAULT_OFFSET)

        return SendMessage().apply {
            this.text = """
                Привет ${update.getUser().firstName} ${update.getUser().lastName}
                
                Я бот с забавными тестами.
                
                Можешь выбрать любой тест и пройти его
            """.trimIndent()
            this.chatId = update.getChat().id.toString()
            this.replyMarkup = InlineKeyboardMarkup().apply {
                this.keyboard = tests.chunked(CHUNK)
                    .map { chunkTests ->
                        chunkTests.map { test ->
                            InlineKeyboardButton().apply {
                                this.text = test.name
                                this.callbackData = "/get_test ${test.id}"
                            }
                        }
                    } + listOf(
                    listOf(
                        InlineKeyboardButton().apply {
                            this.text = "⏮"
                            this.callbackData = "/last_tests"
                        },
                        InlineKeyboardButton().apply {
                            this.text = "⏭"
                            this.callbackData = "/next_tests"
                        },
                    ),
                )
            }
        }
    }
}
