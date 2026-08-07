package io.github.evmetatron.evfunnytest.storage.db.repository

import io.github.evmetatron.evfunnytest.storage.db.entity.TestReplaceViewEntity
import org.springframework.data.repository.CrudRepository

interface TestReplaceViewRepository : CrudRepository<TestReplaceViewEntity, Long>
