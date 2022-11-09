description = "test-handler"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.data:spring-data-redis")
    implementation("redis.clients:jedis")
    implementation("org.flywaydb:flyway-core")
    implementation("com.github.evmetatron:spring-kotlin-chain-of-responsibility:1.0")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation(project(":test-settings"))
    testImplementation("org.testcontainers:junit-jupiter:1.17.3")
    testImplementation("org.testcontainers:testcontainers:1.17.3")
    testImplementation("org.testcontainers:postgresql:1.17.3")
}