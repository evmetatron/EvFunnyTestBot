/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table

@Table("test_question_score")
data class QuestionScoreEntity(
    @Id
    val id: Long,
    val num: Int,
    val question: String,
    val description: String,
    @MappedCollection(idColumn = "question_id", keyColumn = "id")
    val variables: List<QuestionVariableScoreEntity>,
)
