/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package fixtures

import com.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import com.evmetatron.evfunnytest.dto.adapter.EditButtonsAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.UserAdapter
import com.evmetatron.evfunnytest.dto.adapter.textselection.DefaultSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.TextSelection
import com.evmetatron.evfunnytest.dto.button.BaseButton
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.enumerable.ButtonType

fun createInputAdapter(
    chatId: Long = faker.number().randomNumber(),
    messageId: Int = faker.number().randomDigit(),
    text: String? = faker.harryPotter().quote(),
    user: UserAdapter = createUserAdapter(),
    button: BaseButton? = createBaseButton(),
    command: BotCommand? = rndEnum<BotCommand>(),
): InputAdapter =
    InputAdapter(
        chatId = chatId,
        messageId = messageId,
        text = text,
        user = user,
        button = button,
        command = command,
    )

fun createUserAdapter(
    id: Long = faker.number().randomNumber(),
    firstName: String = faker.name().firstName(),
    lastName: String = faker.name().lastName(),
    userName: String? = faker.name().username(),
): UserAdapter =
    UserAdapter(
        id = id,
        firstName = firstName,
        lastName = lastName,
        userName = userName,
    )

fun createButtonAdapter(
    text: String = faker.harryPotter().quote(),
    button: BaseButton = createBaseButton(),
): ButtonAdapter =
    ButtonAdapter(
        text = text,
        button = button,
    )

fun createBaseButton(
    type: ButtonType = rndEnum(),
    data: Map<String, String> = emptyMap(),
): BaseButton =
    BaseButton(
        type = type,
        data = data,
    )

fun createSendMessageAdapter(
    chatId: Long = faker.number().randomNumber(),
    clearButtonsLater: Boolean = false,
    text: List<TextSelection>? = listOf(createDefaultSelection()),
    buttons: List<List<ButtonAdapter>>? = (1..3).map { (1..3).map { createButtonAdapter() } },
): SendMessageAdapter =
    SendMessageAdapter(
        chatId = chatId,
        clearButtonsLater = clearButtonsLater,
        text = text,
        buttons = buttons,
    )

fun createEditButtonsAdapter(
    chatId: Long = faker.number().randomNumber(),
    clearButtonsLater: Boolean = false,
    messageId: Int = faker.number().randomDigit(),
    buttons: List<List<ButtonAdapter>>? = (1..3).map { (1..3).map { createButtonAdapter() } },
): EditButtonsAdapter =
    EditButtonsAdapter(
        chatId = chatId,
        clearButtonsLater = clearButtonsLater,
        messageId = messageId,
        buttons = buttons,
    )

fun createDefaultSelection(
    text: String = faker.harryPotter().quote(),
): DefaultSelection =
    DefaultSelection(
        text = text,
    )
