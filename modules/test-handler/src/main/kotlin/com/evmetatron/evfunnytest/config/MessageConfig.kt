/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 *  Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

@Configuration
internal class MessageConfig {
    @Bean
    fun messageSource(): MessageSource =
        ReloadableResourceBundleMessageSource().apply {
            this.setBasename("classpath:messages/handler")
            this.setDefaultEncoding("UTF-8")
        }
}