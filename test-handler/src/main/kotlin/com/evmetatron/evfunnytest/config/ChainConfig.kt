/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.config

import com.evmetatron.evfunnytest.handler.input.CancelClickHandler
import com.evmetatron.evfunnytest.handler.input.SendAddGenderHandler
import com.evmetatron.evfunnytest.handler.input.GetTestClickHandler
import com.evmetatron.evfunnytest.handler.input.InputHandler
import com.evmetatron.evfunnytest.handler.input.ExitTestHandler
import com.evmetatron.evfunnytest.handler.input.GenderClickHandler
import com.evmetatron.evfunnytest.handler.input.HandleTestHandler
import com.evmetatron.evfunnytest.handler.input.ListCommandHandler
import com.evmetatron.evfunnytest.handler.input.StartTestClickHandler
import com.evmetatron.evfunnytest.handler.test.ReplaceTestHandler
import com.evmetatron.evfunnytest.handler.test.ScoreTestHandler
import com.evmetatron.evfunnytest.handler.test.TestHandler
import com.github.evmetatron.springkotlinchainofresponsibility.factory.ChainFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class ChainConfig {
    @Bean
    fun chainFactory(): ChainFactory =
        ChainFactory("com.evmetatron.evfunnytest")

    @Bean
    fun inputHandler(@Autowired chainFactory: ChainFactory): InputHandler? =
        chainFactory.createChain(
            listOf(
                CancelClickHandler::class,
                ExitTestHandler::class,
                ListCommandHandler::class,
                GetTestClickHandler::class,
                StartTestClickHandler::class,
                GenderClickHandler::class,
                SendAddGenderHandler::class,
                HandleTestHandler::class,
            )
        )

    @Bean
    fun testHandler(@Autowired chainFactory: ChainFactory): TestHandler? =
        chainFactory.createChain(
            listOf(
                ReplaceTestHandler::class,
                ScoreTestHandler::class,
            ),
        )
}
