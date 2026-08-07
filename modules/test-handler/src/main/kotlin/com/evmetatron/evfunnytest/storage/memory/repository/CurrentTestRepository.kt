package com.evmetatron.evfunnytest.storage.memory.repository

import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.springframework.data.repository.CrudRepository

interface CurrentTestRepository : CrudRepository<CurrentTestEntity, Long>
