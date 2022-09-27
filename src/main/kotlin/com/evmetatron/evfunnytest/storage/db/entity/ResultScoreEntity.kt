/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.entity

import org.springframework.data.relational.core.mapping.Table

@Table("test_result_score")
data class ResultScoreEntity(
    val from: Int,
    val to: Int?,
    val result: String,
)
