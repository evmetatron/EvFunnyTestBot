/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.data.db.entity

import com.evmetatron.evfunnytest.enumerable.TestType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("test_view")
data class TestViewEntity(
    @Id
    val id: UUID,
    val name: String,
    val description: String,
    val type: TestType,
)
