/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.exception

class ConvertToDataException(className: String, data: Any) :
    RuntimeException("Не удалось создать объект $className из $data")
