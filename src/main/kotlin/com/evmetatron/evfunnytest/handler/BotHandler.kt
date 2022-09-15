/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler

import com.evmetatron.evfunnytest.storage.memory.repository.CurrentTestRepository
import com.evmetatron.evfunnytest.handler.input.InputHandler
import com.evmetatron.evfunnytest.property.MainProperties
import com.evmetatron.evfunnytest.utils.getUser
import com.evmetatron.evfunnytest.utils.toSendMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
internal class BotHandler(
    private val mainProperties: MainProperties,
    private val currentTestRepository: CurrentTestRepository,
    private val inputHandler: InputHandler?,
) : TelegramLongPollingBot() {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun getBotToken(): String =
        mainProperties.telegram.token

    override fun getBotUsername(): String =
        mainProperties.telegram.name

    override fun onUpdateReceived(update: Update) {
        logger.info("On update $update")

        val currentTest = currentTestRepository.findByIdOrNull(update.getUser().id)

        try {
            val event = inputHandler?.execute(update, currentTest)

            when (event) {
                is SendMessage -> execute(event)
                is EditMessageReplyMarkup -> execute(event)
                else -> {
                    logger.error("No publishers for $update")
                    executeErrorMessage(update)
                }
            }
        } catch (e: TelegramApiException) {
            logger.error(e.stackTraceToString())
            executeErrorMessage(update)
        }
    }

    private fun executeErrorMessage(update: Update) {
        execute(update.toSendMessage("Не удалось обработать запрос"))
    }
}
