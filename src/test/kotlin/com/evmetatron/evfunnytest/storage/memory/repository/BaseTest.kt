/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.memory.repository

import com.evmetatron.evfunnytest.settings.InstanceRedisContainer
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Suppress("UnnecessaryAbstractClass", "UtilityClassWithPublicConstructor")
@Testcontainers
@DataRedisTest
abstract class BaseTest {
    companion object {
        @JvmStatic
        protected val redis: GenericContainer<InstanceRedisContainer> = InstanceRedisContainer.getInstance()
    }
}
