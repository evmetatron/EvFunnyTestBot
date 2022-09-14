/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.entity

import com.evmetatron.evfunnytest.enumerable.TestType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("test")
data class TestEntity(
    @Id
    val id: Long,
    val name: String,
    val description: String,
    val type: TestType,
)
