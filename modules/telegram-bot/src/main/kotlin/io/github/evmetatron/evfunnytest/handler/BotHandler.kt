package io.github.evmetatron.evfunnytest.handler

import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.exception.InternalLogicException
import io.github.evmetatron.evfunnytest.handler.input.InputHandler
import io.github.evmetatron.evfunnytest.property.TelegramProperties
import io.github.evmetatron.evfunnytest.service.CurrentTestService
import io.github.evmetatron.evfunnytest.service.RemoveButtonsService
import io.github.evmetatron.evfunnytest.storage.memory.entity.RemoveButtonsEntity
import io.github.evmetatron.evfunnytest.utils.toInputAdapter
import io.github.evmetatron.evfunnytest.utils.toTelegramMessage
import io.github.evmetatron.evfunnytest.utils.toTelegramSendMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
internal class BotHandler(
    private val telegramProperties: TelegramProperties,
    private val currentTestService: CurrentTestService,
    private val removeButtonsService: RemoveButtonsService,
    private val inputHandler: InputHandler?,
) : TelegramLongPollingBot() {
    private val logger = KotlinLogging.logger {}

    override fun getBotToken(): String =
        telegramProperties.token

    override fun getBotUsername(): String =
        telegramProperties.name

    override fun onUpdateReceived(update: Update): Unit = runBlocking {
        logger.info { "On update $update" }

        val input = update.toInputAdapter()

        val currentTest = currentTestService.getCurrentTest(input.user.id)

        try {
            removeButtonsService.getByUserId(input.user.id)
                ?.apply { clearButtons(this) }

            val event = inputHandler?.getObject(input, currentTest, HandlerContext())

            val message = event?.toTelegramMessage()

            val messageId: Int? = when (message) {
                is SendMessage -> execute(message).messageId
                is EditMessageReplyMarkup -> {
                    (execute(message) as Message).messageId
                }

                else -> {
                    logger.error { "No publishers for $message" }
                    executeErrorMessage(update)
                    null
                }
            }

            if (messageId != null && event?.clearButtonsLater == true) {
                removeButtonsService.registerMessage(input.user.id, input.chatId, messageId)
            }
        } catch (e: TelegramApiException) {
            logger.error(e) { "Telegram api error" }
            executeErrorMessage(update)
        } catch (e: InternalLogicException) {
            logger.error(e) { "Internal logic error" }
            executeErrorMessage(update)
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    fun clearButtonsSchedule() = runBlocking {
        removeButtonsService.findExpired().forEach { clearButtons(it) }
    }

    private suspend fun clearButtons(removeButtonsEntity: RemoveButtonsEntity): Unit = coroutineScope {
        removeButtonsEntity.messageIds.map { messageId ->
            async(Dispatchers.IO) {
                try {
                    execute(
                        EditMessageReplyMarkup().apply {
                            this.chatId = removeButtonsEntity.chatId.toString()
                            this.messageId = messageId
                            this.replyMarkup = null
                        }
                    )
                } catch (e: TelegramApiException) {
                    logger.error(e) { "Telegram api error" }
                }
            }
        }
            .awaitAll()

        removeButtonsService.remove(removeButtonsEntity.userId)
    }

    private fun executeErrorMessage(update: Update) {
        execute(update.toTelegramSendMessage("Не удалось обработать запрос"))
    }
}
