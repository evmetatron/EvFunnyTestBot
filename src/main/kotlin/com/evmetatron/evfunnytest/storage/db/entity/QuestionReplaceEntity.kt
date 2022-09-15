/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.entity

import org.springframework.data.relational.core.mapping.Table

@Table("test_question_replace")
data class QuestionReplaceEntity(
    val num: Int,
    val question: String,
)
