/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.infrastracture

import com.evmetatron.evfunnytest.property.MainProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

@Component
internal class BotHandler(
    private val mainProperties: MainProperties,
) : TelegramLongPollingBot() {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun getBotToken(): String =
        mainProperties.telegram.token

    override fun getBotUsername(): String =
        mainProperties.telegram.name

    override fun onUpdateReceived(update: Update) {
        logger.info("On update $update")
    }
}
