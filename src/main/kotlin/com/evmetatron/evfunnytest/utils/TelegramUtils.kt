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
import com.evmetatron.evfunnytest.dto.adapter.textselection.BoldSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.ItalicSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.TextSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.UnderlineSelection
import com.evmetatron.evfunnytest.dto.button.BaseButton
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.exception.TelegramPropertyException
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

fun TextSelection.getTelegramType(): String? =
    when (this) {
        is BoldSelection -> EntityType.BOLD
        is ItalicSelection -> EntityType.ITALIC
        is UnderlineSelection -> "underline" // В EntityType нет такой константы
        else -> null
    }

fun MessageAdapter.toTelegramMessage(): BotApiMethod<*> =
    when (this) {
        is SendMessageAdapter -> this.toTelegramMessage()
        is EditButtonsAdapter -> this.toTelegramMessage()
    }

private fun SendMessageAdapter.toTelegramMessage(): SendMessage =
    SendMessage().apply {
        val adapter = this@toTelegramMessage
        var textOffset = 0

        adapter.text?.let { texts ->
            this.text = texts.joinToString(separator = "") { it.text }

            if (texts.isNotEmpty()) {
                this.entities = texts.mapNotNull { selection ->
                    val entity = selection.getTelegramType()?.let { type ->
                        MessageEntity().apply {
                            this.type = type
                            this.text = selection.text
                            this.offset = textOffset
                            this.length = selection.text.length
                        }
                    }

                    textOffset += selection.text.length

                    entity
                }
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
