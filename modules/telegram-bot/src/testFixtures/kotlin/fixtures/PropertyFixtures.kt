package fixtures

import com.evmetatron.evfunnytest.property.TelegramProperties

fun createTelegramProperties(
    name: String = faker.name().username(),
    token: String = faker.internet().password(),
) =
    TelegramProperties(
        name = name,
        token = token,
    )
