package io.github.evmetatron.evfunnytest.storage.db.entity

import io.github.evmetatron.evfunnytest.enumerable.AllowGender
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table

@Table("test_score_view")
data class TestScoreViewEntity(
    @Id
    val id: Long,
    val name: String,
    val description: String,
    val allowGender: AllowGender,
    @MappedCollection(idColumn = "test_id", keyColumn = "num")
    val questions: List<QuestionScoreEntity>,
    @MappedCollection(idColumn = "test_id", keyColumn = "id")
    val results: List<ResultScoreEntity>,
)
