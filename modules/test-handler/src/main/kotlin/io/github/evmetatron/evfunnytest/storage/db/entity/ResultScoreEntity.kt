package io.github.evmetatron.evfunnytest.storage.db.entity

import org.springframework.data.relational.core.mapping.Table

@Table("test_result_score")
data class ResultScoreEntity(
    val from: Int,
    val to: Int?,
    val result: String,
)
