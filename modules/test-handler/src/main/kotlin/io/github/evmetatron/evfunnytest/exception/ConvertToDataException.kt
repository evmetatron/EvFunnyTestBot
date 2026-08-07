package io.github.evmetatron.evfunnytest.exception

class ConvertToDataException(className: String, data: Any) :
    InternalLogicException("Не удалось создать объект $className из $data")
