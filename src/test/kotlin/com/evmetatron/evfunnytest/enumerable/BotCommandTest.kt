/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.enumerable

import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

internal class BotCommandTest {
    @ParameterizedTest
    @CsvSource(
        "/start, START",
        "/list, LIST",
        "/cancel, CANCEL",
        "/exit, EXIT",
    )
    fun `get command object`(input: String, expected: BotCommand) {
        BotCommand.getCommandByInput(input) shouldBe expected
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "/somecommand",
            "/list/list",
            "/list /list",
            "/start /list",
            "some text",
        ]
    )
    fun `get null`(input: String) {
        BotCommand.getCommandByInput(input) shouldBe null
    }
}
