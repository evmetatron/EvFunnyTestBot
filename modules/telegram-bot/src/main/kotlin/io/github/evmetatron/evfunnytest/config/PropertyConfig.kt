package io.github.evmetatron.evfunnytest.config

import io.github.evmetatron.evfunnytest.property.TelegramProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    TelegramProperties::class,
)
internal class PropertyConfig
