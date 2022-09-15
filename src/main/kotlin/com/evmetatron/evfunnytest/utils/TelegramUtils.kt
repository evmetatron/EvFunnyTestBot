/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.utils

import com.evmetatron.evfunnytest.dto.ButtonClick
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.exception.TelegramPropertyException
import com.google.gson.Gson
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User

fun Update.getUser(): User =
    message?.from
        ?: callbackQuery?.from
        ?: throw TelegramPropertyException()

fun Update.getChat(): Chat =
    message?.chat
        ?: callbackQuery?.message?.chat
        ?: throw TelegramPropertyException()

fun Update.getBotCommand(): BotCommand? =
    message?.text?.let { BotCommand.getCommandByInput(it) }

fun Update.getButtonClick(): ButtonClick? =
    callbackQuery?.data
        ?.let { Gson().fromJson(it, ButtonClick::class.java) }

fun Update.toSendMessage(text: String): SendMessage =
    SendMessage().apply {
        this.chatId = this@toSendMessage.getChat().id.toString()
        this.text = text
    }
