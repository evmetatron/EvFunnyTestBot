/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.memory.repository

import com.evmetatron.evfunnytest.storage.memory.entity.RemoveButtonsEntity
import org.springframework.data.repository.CrudRepository

interface RemoveButtonsRepository : CrudRepository<RemoveButtonsEntity, Long>
