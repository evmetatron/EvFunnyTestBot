/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.data.db.repository

import com.evmetatron.evfunnytest.data.db.entity.TestViewEntity
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface TestViewRepository : CrudRepository<TestViewEntity, UUID> {
    @Query(
        """
            select *
            from test_view
            order by id
            limit :limit offset :offset
        """
    )
    fun findLimited(
        limit: Int,
        offset: Int,
    ): List<TestViewEntity>
}
