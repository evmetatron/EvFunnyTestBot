/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

abstract class AbstractInputHandler(
    private val inputHandler: InputHandler?,
) : InputHandler {
    override fun execute(
        update: Update,
        currentTestEntity: CurrentTestEntity?,
    ): PartialBotApiMethod<*>? =
        verify(update, currentTestEntity).takeIf { it }
            ?.let { handle(update, currentTestEntity) }
            ?: inputHandler?.execute(update, currentTestEntity)

    protected abstract fun verify(
        update: Update,
        currentTestEntity: CurrentTestEntity?,
    ): Boolean

    protected abstract fun handle(
        update: Update,
        currentTestEntity: CurrentTestEntity?,
    ): PartialBotApiMethod<*>
}
