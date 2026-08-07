package io.github.evmetatron.evfunnytest.storage.db.entity

import io.github.evmetatron.evfunnytest.enumerable.AllowGender
import io.github.evmetatron.evfunnytest.enumerable.TestType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("test")
data class TestEntity(
    @Id
    val id: Long,
    val name: String,
    val description: String,
    val type: TestType,
    val allowGender: AllowGender,
)
