package com.evmetatron.evfunnytest.storage.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("test_question_score_variable")
data class QuestionVariableScoreEntity(
    @Id
    val id: Long,
    val variable: String,
    val isTrue: Boolean,
)
