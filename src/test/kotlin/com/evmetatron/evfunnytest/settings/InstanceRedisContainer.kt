/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.settings

import org.testcontainers.containers.GenericContainer

class InstanceRedisContainer(
    imageName: String,
) : GenericContainer<InstanceRedisContainer>(imageName) {
    companion object {
        private var instance: GenericContainer<InstanceRedisContainer>? = null

        fun getInstance(): GenericContainer<InstanceRedisContainer> =
            instance ?: createInstance()

        private fun createInstance(): GenericContainer<InstanceRedisContainer> {
            instance = InstanceRedisContainer("redis")
            (instance as InstanceRedisContainer).start()

            return instance as InstanceRedisContainer
        }
    }
}
