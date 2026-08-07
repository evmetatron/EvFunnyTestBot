package io.github.evmetatron.evfunnytest.storage.db.entity

import io.github.evmetatron.evfunnytest.enumerable.Gender
import org.springframework.data.relational.core.mapping.Table

@Table("test_result_replace")
data class ResultReplaceEntity(
    val gender: Gender?,
    val result: String,
)
