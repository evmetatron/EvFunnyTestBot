/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.config

import com.evmetatron.evfunnytest.handler.input.SendAddGenderHandler
import com.evmetatron.evfunnytest.handler.input.GetTestClickHandler
import com.evmetatron.evfunnytest.handler.input.InputHandler
import com.evmetatron.evfunnytest.handler.input.ExitTestHandler
import com.evmetatron.evfunnytest.handler.input.GenderClickHandler
import com.evmetatron.evfunnytest.handler.input.HandleTestHandler
import com.evmetatron.evfunnytest.handler.input.ListCommandHandler
import com.evmetatron.evfunnytest.handler.input.StartTestClickHandler
import com.evmetatron.evfunnytest.handler.test.ReplaceTestHandler
import com.evmetatron.evfunnytest.handler.test.TestHandler
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
            ExitTestHandler::class,
            ListCommandHandler::class,
            GetTestClickHandler::class,
            StartTestClickHandler::class,
            GenderClickHandler::class,
            SendAddGenderHandler::class,
            HandleTestHandler::class,
        )

    @Bean
    fun testHandler(@Autowired chainFactory: ChainOfResponsibilityFactory): TestHandler? =
        chainFactory.createChain<TestHandler>(
            ReplaceTestHandler::class,
        )
}
