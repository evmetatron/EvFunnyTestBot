description = "telegram-bot"

dependencies {
    implementation(project(":modules:test-handler"))
    implementation("com.github.xabgesagtx:telegram-spring-boot-starter:0.26")

    testImplementation(testFixtures(project(":modules:test-handler")))

    testFixturesImplementation(testFixtures(project(":modules:test-handler")))
    testFixturesImplementation("org.telegram:telegrambots-meta:5.6.0")
}