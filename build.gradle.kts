import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("io.gitlab.arturbosch.detekt").version("1.21.0")
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.evmetatron"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.data:spring-data-redis")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("redis.clients:jedis")
	implementation("org.flywaydb:flyway-core")
	implementation("com.google.code.gson:gson")
	implementation("com.github.xabgesagtx:telegram-spring-boot-starter:0.26")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5:5.4.1")
	testImplementation("io.mockk:mockk:1.12.5")
	testImplementation("com.github.javafaker:javafaker:1.0.2"){
		exclude("org.yaml")
	}
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.junit.jupiter:junit-jupiter-params")
	testImplementation("org.testcontainers:testcontainers:1.17.3")
	testImplementation("org.testcontainers:postgresql:1.17.3")
	testImplementation("org.testcontainers:junit-jupiter:1.17.3")
}

detekt {
	source = files("$rootDir/src/main/kotlin", "$rootDir/src/test/kotlin")
	config = files("$rootDir/detekt/detekt.yaml")
	buildUponDefaultConfig = true
	autoCorrect = true

	dependencies {
		detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.21.0")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = JavaVersion.VERSION_17.toString()
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
