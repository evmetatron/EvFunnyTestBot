/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.dto.adapter

data class UserAdapter(
    val id: Long,
    val firstName: String,
    val lastName: String?,
    val userName: String?,
) {
    fun toName(): String =
        listOfNotNull(
            this.firstName,
            this.lastName,
        ).joinToString(separator = " ") { it }
}
