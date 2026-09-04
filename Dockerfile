# syntax=docker/dockerfile:1

# --- build ---------------------------------------------------------------------
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# Контекст сборки уже отфильтрован .dockerignore (нет .git, build/, .env* и т.п.).
# Кэш зависимостей Gradle переживает пересборки через cache-mount ниже.
COPY . .
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
