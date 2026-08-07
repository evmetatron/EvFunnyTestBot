package fixtures

import com.github.javafaker.Faker
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

val faker = Faker(Locale("ru"))

inline fun <reified T : Enum<T>> rndEnum(): T = T::class.java.enumConstants.let {
    it[faker.random().nextInt(it.size)]
}

fun Date.toLocalDate(): LocalDate = toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

/**
 * Оборачивает значение в Optional для мока Spring Data `findById(): Optional<T>`.
 * Используется только на границе с MockK-стабами — сам Optional наружу не течёт.
 */
fun <T : Any> T?.asOptional(): Optional<T> = Optional.ofNullable(this)
