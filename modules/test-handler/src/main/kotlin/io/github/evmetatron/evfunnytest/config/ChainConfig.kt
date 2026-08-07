package io.github.evmetatron.evfunnytest.config

import io.github.evmetatron.evfunnytest.handler.input.InputHandler
import io.github.evmetatron.evfunnytest.handler.test.TestHandler
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
