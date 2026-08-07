package io.github.evmetatron.evfunnytest.storage.memory.repository

import io.github.evmetatron.evfunnytest.storage.memory.entity.RemoveButtonsEntity
import org.springframework.data.repository.CrudRepository

interface RemoveButtonsRepository : CrudRepository<RemoveButtonsEntity, Long>
