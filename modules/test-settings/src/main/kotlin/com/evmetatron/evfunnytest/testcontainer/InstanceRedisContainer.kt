/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.testcontainer

import org.testcontainers.containers.GenericContainer

class InstanceRedisContainer(
    imageName: String,
) : GenericContainer<InstanceRedisContainer>(imageName) {
    companion object {
        private const val PORT = 6379
        private var instance: GenericContainer<InstanceRedisContainer>? = null

        fun getInstance(): GenericContainer<InstanceRedisContainer> =
            instance ?: createInstance()

        private fun createInstance(): GenericContainer<InstanceRedisContainer> {
            instance = InstanceRedisContainer("redis")
                .withExposedPorts(PORT)

            val redis = instance as InstanceRedisContainer

            redis.start()

            System.setProperty("spring.redis.host", redis.host)
            System.setProperty("spring.redis.port", redis.getMappedPort(PORT).toString())

            return redis
        }
    }
}
