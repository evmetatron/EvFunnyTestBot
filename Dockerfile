# syntax=docker/dockerfile:1

# --- build ---------------------------------------------------------------------
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# Сначала только то, что влияет на резолв зависимостей — кэш слоя переживает правки кода.
COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle.kts build.gradle.kts ./
COPY detekt ./detekt
COPY modules/test-handler/build.gradle.kts ./modules/test-handler/
COPY modules/telegram-bot/build.gradle.kts ./modules/telegram-bot/
COPY modules/test-settings/build.gradle.kts ./modules/test-settings/
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon dependencies -q || true

COPY modules ./modules
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon -x test -x detekt :modules:telegram-bot:bootJar

# --- runtime -----------------------------------------------------------------
FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app

RUN groupadd --system app && useradd --system --gid app --home /app app

COPY --from=build /app/modules/telegram-bot/build/libs/app.jar ./app.jar

USER app

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
