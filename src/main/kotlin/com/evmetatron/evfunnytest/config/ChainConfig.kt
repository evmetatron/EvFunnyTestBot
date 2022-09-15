/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.config

import com.evmetatron.evfunnytest.handler.input.GetTestClickHandler
import com.evmetatron.evfunnytest.handler.input.InputHandler
import com.evmetatron.evfunnytest.handler.input.ListCommandHandler
import com.evmetatron.evfunnytest.infrastructure.ChainOfResponsibilityFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class ChainConfig {
    @Bean
    fun chainFactory(): ChainOfResponsibilityFactory =
        ChainOfResponsibilityFactory()

    @Bean
    fun inputHandler(@Autowired chainFactory: ChainOfResponsibilityFactory): InputHandler? =
        chainFactory.createChain<InputHandler>(
            ListCommandHandler::class,
            GetTestClickHandler::class,
        )
}
