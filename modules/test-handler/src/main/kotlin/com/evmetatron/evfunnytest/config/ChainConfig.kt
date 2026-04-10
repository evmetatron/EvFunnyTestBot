/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.config

import com.evmetatron.evfunnytest.handler.input.InputHandler
import com.evmetatron.evfunnytest.handler.test.TestHandler
import io.github.evmetatron.spring.cor.ChainFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class ChainConfig {
    @Bean
    fun inputHandler(chainFactory: ChainFactory): InputHandler =
        chainFactory.createChain(InputHandler::class.java)

    @Bean
    fun testHandler(chainFactory: ChainFactory): TestHandler =
        chainFactory.createChain(TestHandler::class.java)
}
