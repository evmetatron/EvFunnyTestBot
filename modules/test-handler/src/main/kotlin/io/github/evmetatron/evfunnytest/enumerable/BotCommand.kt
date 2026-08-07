package io.github.evmetatron.evfunnytest.enumerable

enum class BotCommand {
    START,
    LIST,
    EXIT;

    companion object {
        fun getCommandByInput(input: String): BotCommand? =
            "^/(\\w+)$".toRegex().find(input)
                ?.groups
                ?.last()
                ?.value
                ?.uppercase()
                ?.takeIf { command ->
                    command in values().map { it.name }
                }
                ?.let { valueOf(it) }
    }
}
