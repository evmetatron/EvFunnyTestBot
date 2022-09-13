/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.config

import com.evmetatron.evfunnytest.handler.input.CancelCommandHandler
import com.evmetatron.evfunnytest.handler.input.ExitCommandHandler
import com.evmetatron.evfunnytest.handler.input.InputHandler
import com.evmetatron.evfunnytest.handler.input.ListCommandHandler
import com.evmetatron.evfunnytest.infrastructure.ChainOfResponsibilityFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class InputHandlerConfig {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Bean
    fun inputHandler(): InputHandler? =
        ChainOfResponsibilityFactory<InputHandler>(applicationContext).createChain(
            ListCommandHandler::class,
            CancelCommandHandler::class,
            ExitCommandHandler::class,
        )
}
