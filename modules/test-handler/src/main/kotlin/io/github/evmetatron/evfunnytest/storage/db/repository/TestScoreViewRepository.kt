package io.github.evmetatron.evfunnytest.storage.db.repository

import io.github.evmetatron.evfunnytest.storage.db.entity.TestScoreViewEntity
import org.springframework.data.repository.CrudRepository

interface TestScoreViewRepository : CrudRepository<TestScoreViewEntity, Long>
