/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.testcontainer

import org.testcontainers.containers.PostgreSQLContainer

class InstancePostgresContainer(
    imageName: String,
) : PostgreSQLContainer<InstancePostgresContainer>(imageName) {
    companion object {
        private var instance: PostgreSQLContainer<InstancePostgresContainer>? = null

        fun getInstance(): PostgreSQLContainer<InstancePostgresContainer> =
            instance ?: createInstance()

        private fun createInstance(): PostgreSQLContainer<InstancePostgresContainer> {
            instance = InstancePostgresContainer("postgres:14")
                .withDatabaseName("database-test")
                .withUsername("test")
                .withUsername("test")

            return instance as InstancePostgresContainer
        }
    }

    override fun stop() {
        // Нельзя стопить, иначе контейнеры пересоздадутся
    }
}
