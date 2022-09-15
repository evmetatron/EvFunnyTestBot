/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.entity

import com.evmetatron.evfunnytest.enumerable.Gender
import org.springframework.data.relational.core.mapping.Table

@Table("test_result_replace")
data class ResultReplaceEntity(
    val gender: Gender,
    val result: String,
)
