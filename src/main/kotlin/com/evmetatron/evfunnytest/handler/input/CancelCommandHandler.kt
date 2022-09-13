/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.data.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.data.memory.repository.CurrentTestRepository
import com.evmetatron.evfunnytest.enumerable.BotCommand
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

class CancelCommandHandler(
    private val currentTestRepository: CurrentTestRepository,
    private val inputHandler: InputHandler?,
) : InputHandler {
    override fun execute(
        update: Update,
        currentTestEntity: CurrentTestEntity?,
        botCommand: BotCommand?,
    ): PartialBotApiMethod<*>? {
        TODO("TODO")
    }
}
