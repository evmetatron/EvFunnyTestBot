# Бот с тестами для Telegram

## Местонахождение бота

Телеграм бот находится в модуле **telegram-bot**

## Переменные окружения

**EV_BOT_NAME** - Имя бота

**EV_BOT_TOKEN** - Токен бота

**EV_POSTGRES_DB** - Название базы данных в Postgres

**EV_POSTGRES_USERNAME** - Имя пользователя в Postgres

**EV_POSTGRES_PASSWORD** - Пароль в Postgres

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