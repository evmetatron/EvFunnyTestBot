package io.github.evmetatron.evfunnytest.dto.button

import com.google.gson.Gson
import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.exception.ConvertToDataException

data class BaseButton(
    val type: ButtonType,
    val data: Map<String, String> = emptyMap(),
) {
    fun toJson(): String =
        GSON.toJson(this)

    fun toConcreteButton(): ConcreteButton? =
        type.getConcreteButton(data)

    companion object {
        private val GSON = Gson()

        fun fromJson(json: String): BaseButton =
            GSON.fromJson(json, BaseButton::class.java)
    }
}

/**
 * Возвращает конкретную кнопку ожидаемого типа [T] или бросает [ConvertToDataException],
 * если кнопки нет или её тип не совпал — вместо непрозрачного `ClassCastException`/`NPE` от `as`.
 */
inline fun <reified T : ConcreteButton> BaseButton?.toConcreteButtonAs(): T =
    this?.toConcreteButton() as? T
        ?: throw ConvertToDataException(
            T::class.simpleName ?: "ConcreteButton",
            this?.data ?: emptyMap<String, String>(),
        )
