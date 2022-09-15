/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.db.repository

import com.evmetatron.evfunnytest.storage.db.entity.TestReplaceViewEntity
import org.springframework.data.repository.CrudRepository

interface TestReplaceViewRepository : CrudRepository<TestReplaceViewEntity, Long>
