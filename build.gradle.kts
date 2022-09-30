import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("java-test-fixtures")
	id("io.gitlab.arturbosch.detekt") version "1.21.0"
	id("org.springframework.boot") version "2.7.3"
	id("io.spring.dependency-management") version "1.0.13.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.evmetatron"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
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

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
		implementation("com.google.code.gson:gson")

		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("io.kotest:kotest-runner-junit5:5.4.1")
		testImplementation("io.mockk:mockk:1.12.5")
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
			"$rootDir/test-handler/src/main/kotlin",
			"$rootDir/test-handler/src/test/kotlin",
			"$rootDir/test-handler/src/testFixtures/kotlin",
			"$rootDir/telegram-bot/src/main/kotlin",
			"$rootDir/telegram-bot/src/test/kotlin",
			"$rootDir/telegram-bot/src/testFixtures/kotlin",
		)
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
			jvmTarget = JavaVersion.VERSION_11.toString()
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}
