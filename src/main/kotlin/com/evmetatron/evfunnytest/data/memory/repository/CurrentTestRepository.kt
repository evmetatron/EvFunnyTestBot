/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.data.memory.repository

import com.evmetatron.evfunnytest.data.memory.entity.CurrentTestEntity
import org.springframework.data.repository.CrudRepository

interface CurrentTestRepository : CrudRepository<CurrentTestEntity, Long>
