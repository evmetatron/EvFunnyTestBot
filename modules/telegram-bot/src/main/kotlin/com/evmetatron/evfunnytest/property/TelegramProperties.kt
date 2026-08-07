package com.evmetatron.evfunnytest.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("properties.telegram")
data class TelegramProperties(
    val name: String,
    val token: String,
)
