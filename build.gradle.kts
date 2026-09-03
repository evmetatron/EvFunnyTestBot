import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("java-test-fixtures")
	id("io.gitlab.arturbosch.detekt") version "1.23.8"
	id("org.springframework.boot") version "2.7.18"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("jvm") version "2.0.21"
	kotlin("plugin.spring") version "2.0.21"
}

group = "io.github.evmetatron"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

// Корневой проект — чистый агрегатор модулей, своего @SpringBootApplication
// у него нет и не должно быть, поэтому bootJar здесь тоже отключаем.
tasks.named("bootJar") {
	enabled = false
}
tasks.named("jar") {
	enabled = true
}

subprojects {
	apply {
		plugin("io.gitlab.arturbosch.detekt")
		plugin("java-test-fixtures")
		plugin("org.springframework.boot")
		plugin("io.spring.dependency-management")
		plugin("org.jetbrains.kotlin.jvm")
		plugin("org.jetbrains.kotlin.plugin.spring")
	}

	java.sourceCompatibility = JavaVersion.VERSION_17

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
		implementation("com.google.code.gson:gson")
		implementation("io.github.oshai:kotlin-logging-jvm:7.0.14")

		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
		testImplementation("io.mockk:mockk:1.14.11")
		testImplementation("org.junit.jupiter:junit-jupiter")
		testImplementation("org.junit.jupiter:junit-jupiter-api")
		testImplementation("org.junit.jupiter:junit-jupiter-engine")
		testImplementation("org.junit.jupiter:junit-jupiter-params")
		testImplementation("com.github.javafaker:javafaker:1.0.2") {
			exclude("org.yaml")
		}

		testFixturesImplementation("com.github.javafaker:javafaker:1.0.2"){
			exclude("org.yaml")
		}
	}

	detekt {
		source = files(
			"$rootDir/modules/test-handler/src",
			"$rootDir/modules/telegram-bot/src",
			"$rootDir/modules/test-settings/src",
		)
		config = files("$rootDir/detekt/detekt.yaml")
		buildUponDefaultConfig = true
		autoCorrect = true

		dependencies {
			detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
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
		jvmArgs(
			"--add-opens", "java.base/java.lang=ALL-UNNAMED",
			"--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
			"--add-opens", "java.base/java.util=ALL-UNNAMED",
			"--add-opens", "java.base/java.time=ALL-UNNAMED",
		)
	}

	// bootJar (исполняемый jar) осмыслен только для приложения-точки-входа.
	// test-handler и test-settings — библиотечные модули без @SpringBootApplication,
	// им bootJar не нужен и без main-класса он падает.
	if (name != "telegram-bot") {
		tasks.named("bootJar") {
			enabled = false
		}
		tasks.named("jar") {
			enabled = true
		}
	}
}
