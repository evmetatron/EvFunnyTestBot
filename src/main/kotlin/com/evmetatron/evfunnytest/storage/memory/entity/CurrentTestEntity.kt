/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.memory.entity

import com.evmetatron.evfunnytest.enumerable.TestType
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("current_test", timeToLive = 7200)
data class CurrentTestEntity(
    @Id
    val userId: Long,
    val testId: Long,
    val type: TestType,
    val answers: List<CurrentAnswerEntity> = emptyList(),
)
