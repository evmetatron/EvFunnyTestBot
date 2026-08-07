package io.github.evmetatron.evfunnytest.dto.button

import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import com.google.gson.Gson

data class BaseButton(
    val type: ButtonType,
    val data: Map<String, String> = emptyMap(),
) {
    fun toJson(): String =
        Gson().toJson(this)

    fun toConcreteButton(): ConcreteButton? =
        type.getConcreteButton(data)
}
