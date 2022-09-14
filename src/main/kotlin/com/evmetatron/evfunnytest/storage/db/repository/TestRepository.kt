/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.repository

import com.evmetatron.evfunnytest.storage.db.entity.TestEntity
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface TestRepository : CrudRepository<TestEntity, UUID> {
    @Query(
        """
            select *
            from test
            order by id
            limit :limit offset :offset
        """
    )
    fun findLimited(
        limit: Int,
        offset: Int,
    ): List<TestEntity>
}
