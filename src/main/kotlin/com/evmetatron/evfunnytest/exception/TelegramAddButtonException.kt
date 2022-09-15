/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.exception

import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class TelegramAddButtonException(sendMessage: SendMessage) :
    InternalLogicException("Ошибка при добавлении кнопок в сообщение для объекта $sendMessage")
