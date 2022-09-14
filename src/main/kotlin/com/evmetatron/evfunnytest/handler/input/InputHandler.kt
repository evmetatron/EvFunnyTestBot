/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

interface InputHandler {
    fun execute(update: Update, currentTestEntity: CurrentTestEntity?): PartialBotApiMethod<*>?
}
