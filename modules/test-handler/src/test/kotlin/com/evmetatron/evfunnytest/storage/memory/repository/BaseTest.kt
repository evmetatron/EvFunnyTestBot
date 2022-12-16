/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.memory.repository

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Testcontainers
import com.evmetatron.evfunnytest.testcontainer.InstanceRedisContainer

@Suppress("UnnecessaryAbstractClass", "UtilityClassWithPublicConstructor")
@ContextConfiguration(classes = [CrudRepository::class])
@EnableAutoConfiguration
@ComponentScan
@Testcontainers
@DataRedisTest
abstract class BaseTest {
    companion object {
        @JvmStatic
        protected val redis: GenericContainer<InstanceRedisContainer> = InstanceRedisContainer.getInstance()
    }
}
