package io.github.evmetatron.evfunnytest.handler.input

import io.github.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.InputAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import io.github.evmetatron.evfunnytest.dto.button.GetTestButton
import io.github.evmetatron.evfunnytest.dto.button.StartTestButton
import io.github.evmetatron.evfunnytest.dto.button.toConcreteButtonAs
import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.service.TestService
import io.github.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Suppress("MagicNumber")
@Order(4)
@Component
class GetTestClickHandler(
    private val testService: TestService,
) : AbstractInputHandler() {
    companion object {
        const val BUTTON_TEXT = "Начать тест"
        const val TEST_NOT_FOUND = "Тест не найден"
    }

    override fun verify(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): Boolean {
        val isEmptyCurrentTest = currentTestEntity == null
        val isGetTest = inputAdapter.button?.type == ButtonType.GET_TEST

        return isEmptyCurrentTest && isGetTest
    }

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity?,
        context: HandlerContext,
    ): MessageAdapter {
        val getTestButton = inputAdapter.button.toConcreteButtonAs<GetTestButton>()

        val test = testService.getTest(getTestButton.testId)
            ?: return inputAdapter.toSendMessage(TEST_NOT_FOUND)

        return SendMessageAdapter(
            chatId = inputAdapter.chatId,
            text = "[b]${test.name}[/b]\n\n[i]${test.description}[/i]",
            buttons = listOf(
                listOf(
                    ButtonAdapter(
                        text = BUTTON_TEXT,
                        button = StartTestButton(
                            testId = test.id,
                        ).toBaseButton(),
                    ),
                ),
            ),
        )
    }
}
