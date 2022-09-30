/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package fixtures

import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberLeft
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberMember

fun createUpdate(
    updateId: Int = faker.number().randomDigit(),
    message: Message? = createMessage(),
    callbackQuery: CallbackQuery? = createCallbackQuery(),
    myChatMember: ChatMemberUpdated = createChatMemberUpdated(),
) =
    Update().apply {
        this.updateId = updateId
        this.message = message
        this.callbackQuery = callbackQuery
        this.myChatMember = myChatMember
    }

fun createMessage(
    messageId: Int = faker.number().randomDigit(),
    chat: Chat = createChat(),
    text: String? = faker.harryPotter().quote(),
    replyToMessage: Message? = null,
    from: User = createUser(),
) =
    Message().apply {
        this.messageId = messageId
        this.chat = chat
        this.text = text
        this.replyToMessage = replyToMessage
        this.from = from
    }

fun createUser(
    id: Long = faker.number().randomNumber(),
    userName: String = faker.name().username(),
    firstName: String = faker.name().firstName(),
    lastName: String = faker.name().lastName(),
) =
    User().apply {
        this.id = id
        this.userName = userName
        this.firstName = firstName
        this.lastName = lastName
    }

fun createChat(
    id: Long = faker.number().randomNumber(),
    title: String = faker.harryPotter().character(),
    type: String = listOf("private", "group", "channel", "supergroup").random(),
) =
    Chat().apply {
        this.id = id
        this.title = title
        this.type = type
    }

fun createChatMemberUpdated(
    newChatMember: ChatMember = createChatMemberMember(),
    oldChatMember: ChatMember = createChatMemberLeft(),
    chat: Chat = createChat(),
) =
    ChatMemberUpdated().apply {
        this.newChatMember = newChatMember
        this.oldChatMember = oldChatMember
        this.chat = chat
    }

fun createChatMemberMember() =
    ChatMemberMember()

fun createChatMemberLeft() =
    ChatMemberLeft()

fun createCallbackQuery(
    data: String = createBaseButton().toJson(),
    message: Message? = createMessage(),
    from: User = createUser(),
): CallbackQuery =
    CallbackQuery().apply {
        this.data = data
        this.message = message
        this.from = from
    }
