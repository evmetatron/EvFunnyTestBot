/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.memory.repository

import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.springframework.data.repository.CrudRepository

interface CurrentTestRepository : CrudRepository<CurrentTestEntity, Long>
