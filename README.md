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