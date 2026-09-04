# Бот с тестами для Telegram

## Местонахождение бота

Телеграм бот находится в модуле **telegram-bot**

## Переменные окружения

**EV_BOT_NAME** - Имя бота

**EV_BOT_TOKEN** - Токен бота

**EV_POSTGRES_DB** - Название базы данных в Postgres

**EV_POSTGRES_USERNAME** - Имя пользователя в Postgres

**EV_POSTGRES_PASSWORD** - Пароль в Postgres

### Необязательные

Нужны при запуске против внешних Postgres/Redis (например, в Kubernetes, см. #35).
При локальном запуске через `docker-compose` дефолтов достаточно; `EV_POSTGRES_PORT`
и `EV_REDIS_PORT` также управляют портами, которые публикует `docker-compose`.

**EV_POSTGRES_HOST** - Хост Postgres (по умолчанию `localhost`)

**EV_POSTGRES_PORT** - Порт Postgres (по умолчанию `5432`)

**EV_REDIS_HOST** - Хост Redis (по умолчанию `localhost`)

**EV_REDIS_PORT** - Порт Redis (по умолчанию `6379`)

**EV_REDIS_PASSWORD** - Пароль Redis (по умолчанию пустой; локальный Redis в `docker-compose`
поднимается без авторизации, переменная предназначена для внешнего Redis)

## Перед запуском приложения

Скопировать файл .env.local в .env

````
cp .env.local .env
````

## Запуск локальной инфраструктуры

Запустить можно, выполнив следующую команду:

````
docker-compose -p evfunnytest -f docker-compose.yml up --build -d
````

## Запуск линтера

````
./gradlew detekt
````

## Релиз

Actions → **Release** → **Run workflow**. Можно выбрать тип версии (`auto` — по
[Conventional Commits](https://www.conventionalcommits.org/) с прошлого релиза,
либо принудительно `patch`/`minor`/`major`).

Workflow сам:

1. Считает следующую версию (`git-cliff`, конфиг — `cliff.toml`).
2. Проставляет её в `version` корневого `build.gradle.kts`.
3. Перегенерирует `CHANGELOG.md` целиком.
4. Коммитит и тегает `vX.Y.Z` прямо в `master`.
5. Публикует запись в [GitHub Releases](../../releases) с текстом из changelog.

Пуш тега `vX.Y.Z` запускает `main.yml`: сборка образа с этим тегом → пуш в GHCR →
деплой на VPS, если настроен секрет `KUBE_CONFIG` (см. `deploy/README.md`) — без
него джоба деплоя пропускает себя с предупреждением, остальной пайплайн не падает.

**Чтобы пуш тега реально триггерил `main.yml`**, нужен секрет репозитория
`RELEASE_PAT` — Personal Access Token (fine-grained, права `Contents: Read and write`
на этот репозиторий). Пуш от встроенного `GITHUB_TOKEN` другие workflow не
запускает — это защита GitHub от рекурсии. Без `RELEASE_PAT` релиз всё равно
соберётся (версия, changelog, тег, GitHub Release), но `main.yml` для этого тега
не запустится сам. `workflow_dispatch` тут не поможет: `build-image` публикует
образ только на настоящий push (см. `main.yml`), поэтому единственный рабочий
фолбэк — запушить уже существующий тег ещё раз, но от своего аккаунта:

```bash
git fetch --tags origin
git push origin refs/tags/vX.Y.Z
```

Такой push (не от `GITHUB_TOKEN`) триггерит `main.yml` как обычно.

Коммиты/заголовки PR стоит вести в формате Conventional Commits
(`feat: ...`, `fix: ...`, `refactor: ...`, `chore: ...`) — иначе они попадут
в changelog единым списком без группировки по типу.