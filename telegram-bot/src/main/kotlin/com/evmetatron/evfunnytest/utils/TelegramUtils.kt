/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.utils

import com.evmetatron.evfunnytest.dto.adapter.EditButtonsAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.UserAdapter
import com.evmetatron.evfunnytest.dto.button.BaseButton
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.exception.TelegramPropertyException
import com.evmetatron.evfunnytest.exception.TelegramSelectionTypeNotFound
import com.google.gson.Gson
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.EntityType
import org.telegram.telegrambots.meta.api.objects.MessageEntity
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

private const val BB_PATTERN = "\\[(\\w)\\](.*?)\\[/\\w\\]"
private const val BB_INDEX_MATCH = 0
private const val BB_INDEX_SELECTION = 1
private const val BB_INDEX_WORD = 2

fun Update.getChat(): Chat =
    message?.chat
        ?: callbackQuery?.message?.chat
        ?: throw TelegramPropertyException()

fun Update.toTelegramSendMessage(text: String): SendMessage =
    SendMessage().apply {
        this.chatId = this@toTelegramSendMessage.getChat().id.toString()
        this.text = text
    }

fun Update.toInputAdapter(): InputAdapter =
    InputAdapter(
        chatId = message?.chat?.id
            ?: callbackQuery?.message?.chat?.id
            ?: throw TelegramPropertyException(),
        messageId = message?.messageId
            ?: callbackQuery?.message?.messageId
            ?: throw TelegramPropertyException(),
        text = message?.text ?: callbackQuery.message?.text,
        user = (message?.from ?: callbackQuery?.from)?.let { user ->
            UserAdapter(
                id = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                userName = user.userName,
            )
        } ?: throw TelegramPropertyException(),
        button = callbackQuery?.data
            ?.let { Gson().fromJson(it, BaseButton::class.java) },
        command = message?.text?.let { BotCommand.getCommandByInput(it) },
    )

fun MessageAdapter.toTelegramMessage(): BotApiMethod<*> =
    when (this) {
        is SendMessageAdapter -> this.toTelegramMessage()
        is EditButtonsAdapter -> this.toTelegramMessage()
    }

private fun SendMessageAdapter.toTelegramMessage(): SendMessage =
    SendMessage().apply {
        val adapter = this@toTelegramMessage

        adapter.text?.let { text ->
            var garbageSize = 0

            this.text = text.replace(BB_PATTERN.toRegex(), "$2")

            this.entities = BB_PATTERN.toRegex().findAll(text).toList()
                .map { matches ->
                    val match = matches.groups[BB_INDEX_MATCH]!!
                    val selection = matches.groups[BB_INDEX_SELECTION]!!.value
                    val words = matches.groups[BB_INDEX_WORD]!!.value

                    val entity = MessageEntity().apply {
                        this.type = getTelegramType(selection)
                        this.text = words
                        this.offset = match.range.first - garbageSize
                        this.length = words.length
                    }

                    garbageSize += (match.value.length - words.length)

                    entity
                }
        }

        adapter.buttons?.let { buttons ->
            this.replyMarkup = InlineKeyboardMarkup().apply {
                this.keyboard = buttons.map { rows ->
                    rows.map { button ->
                        InlineKeyboardButton().apply {
                            this.text = button.text
                            this.callbackData = button.button.toJson()
                        }
                    }
                }
            }
        }

        this.chatId = adapter.chatId.toString()
    }

private fun EditButtonsAdapter.toTelegramMessage(): EditMessageReplyMarkup =
    EditMessageReplyMarkup().apply {
        val adapter = this@toTelegramMessage

        adapter.buttons?.let { buttons ->
            this.replyMarkup = InlineKeyboardMarkup().apply {
                this.keyboard = buttons.map { rows ->
                    rows.map { button ->
                        InlineKeyboardButton().apply {
                            this.text = button.text
                            this.callbackData = button.button.toJson()
                        }
                    }
                }
            }
        }

        this.chatId = adapter.chatId.toString()
        this.messageId = adapter.messageId
    }

private fun getTelegramType(bb: String): String =
    when (bb) {
        "b" -> EntityType.BOLD
        "i" -> EntityType.ITALIC
        "u" -> "underline" // В EntityType нет такой константы
        else -> throw TelegramSelectionTypeNotFound(bb)
    }
