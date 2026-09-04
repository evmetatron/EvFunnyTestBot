import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "telegram-bot"

// Детерминированное имя исполняемого jar, чтобы Dockerfile мог копировать его по фиксированному пути.
tasks.named<BootJar>("bootJar") {
    archiveFileName.set("app.jar")
}

dependencies {
    implementation(project(":modules:test-handler"))
    implementation("com.github.xabgesagtx:telegram-spring-boot-starter:0.26")

    testImplementation(testFixtures(project(":modules:test-handler")))

    testFixturesImplementation(testFixtures(project(":modules:test-handler")))
    testFixturesImplementation("org.telegram:telegrambots-meta:5.7.1")
}