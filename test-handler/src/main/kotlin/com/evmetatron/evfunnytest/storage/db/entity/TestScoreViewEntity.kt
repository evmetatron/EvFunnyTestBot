/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.entity

import com.evmetatron.evfunnytest.enumerable.AllowGender
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
