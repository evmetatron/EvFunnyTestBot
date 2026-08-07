package com.evmetatron.evfunnytest.dto.button

import com.evmetatron.evfunnytest.enumerable.ButtonType
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
