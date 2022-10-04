/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.exception

class TelegramSelectionTypeNotFound(bb: String) : InternalLogicException("Не удалось тип выделения текста '$bb'")
