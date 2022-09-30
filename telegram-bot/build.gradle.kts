description = "telegram-bot"

dependencies {
    implementation(project(":test-handler"))
    implementation("com.github.xabgesagtx:telegram-spring-boot-starter:0.26")

    testImplementation(testFixtures(project(":test-handler")))

    testFixturesImplementation(testFixtures(project(":test-handler")))
    testFixturesImplementation("org.telegram:telegrambots-meta:5.6.0")
}