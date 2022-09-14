/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.fixtures

import com.evmetatron.evfunnytest.property.MainProperties
import com.evmetatron.evfunnytest.property.TelegramProperties

fun createMainProperties(
    telegram: TelegramProperties = createTelegramProperties(),
): MainProperties =
    MainProperties(
        telegram = telegram,
    )

fun createTelegramProperties(
    name: String = faker.name().username(),
    token: String = faker.internet().password(),
) =
    TelegramProperties(
        name = name,
        token = token,
    )
