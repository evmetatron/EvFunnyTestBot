package io.github.evmetatron.evfunnytest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TelegramBotApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<TelegramBotApplication>(*args)
}
