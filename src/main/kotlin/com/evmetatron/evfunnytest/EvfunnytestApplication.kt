/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EvfunnytestApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<EvfunnytestApplication>(*args)
}
