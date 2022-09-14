/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.fixtures

import com.github.javafaker.Faker
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

val faker = Faker(Locale("ru"))

inline fun <reified T : Enum<T>> rndEnum(): T = T::class.java.enumConstants.let {
    it[faker.random().nextInt(it.size)]
}

fun Date.toLocalDate(): LocalDate = toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
