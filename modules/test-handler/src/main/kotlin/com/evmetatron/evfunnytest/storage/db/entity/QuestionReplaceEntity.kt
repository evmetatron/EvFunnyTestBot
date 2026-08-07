package com.evmetatron.evfunnytest.storage.db.entity

import org.springframework.data.relational.core.mapping.Table

@Table("test_question_replace")
data class QuestionReplaceEntity(
    val num: Int,
    val question: String,
)
