/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.textselection.DefaultSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.UnderlineSelection
import com.evmetatron.evfunnytest.dto.button.GenderButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.AllowGender
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.enumerable.Gender
import fixtures.createBaseButton
import fixtures.createCurrentTestEntity
import fixtures.createInputAdapter
import fixtures.createSendMessageAdapter
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(MockKExtension::class)
internal class SendAddGenderHandlerTest {
    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    private lateinit var sendAddGenderHandler: SendAddGenderHandler

    private companion object {
        @JvmStatic
        private fun verifyFalseProvider() =
            listOf(
                // CurrentTestEntity не создан
                Arguments.of(
                    null,
                ),

                // Пол уже заполнен
                Arguments.of(
                    createCurrentTestEntity(
                        gender = Gender.FEMALE,
                        allowGender = AllowGender.FOR_ONE,
                    ),
                ),

                // Тест для всех полов
                Arguments.of(
                    createCurrentTestEntity(
                        gender = null,
                        allowGender = AllowGender.ALL,
                    ),
                ),
            )

        @JvmStatic
        private fun getObjectProvider() =
            listOf(
                Arguments.of(
                    createInputAdapter(
                        button = createBaseButton(type = ButtonType.START_TEST),
                    ),
                    HandlerContext().withHandledStart(),
                    SendAddGenderHandler.START_TEST_TEXT,
                ),

                Arguments.of(
                    createInputAdapter(
                        button = createBaseButton(type = ButtonType.CANCEL_ANSWER),
                    ),
                    HandlerContext(),
                    SendAddGenderHandler.CANCEL_ANSWER_TEXT,
                ),

                Arguments.of(
                    createInputAdapter(
                        button = null,
                    ),
                    HandlerContext(),
                    SendAddGenderHandler.ERROR_MESSAGE_TEXT,
                ),
            )
    }

    @ParameterizedTest
    @MethodSource("verifyFalseProvider")
    fun `verify false`(currentTestEntity: CurrentTestEntity?) {
        val inputAdapter = createInputAdapter()
        val sendMessage = createSendMessageAdapter()
        val context = HandlerContext()

        every { inputHandler.getObject(inputAdapter, currentTestEntity, context) } returns sendMessage

        sendAddGenderHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe sendMessage

        verify(exactly = 1) { inputHandler.getObject(inputAdapter, currentTestEntity, any()) }
    }

    @ParameterizedTest
    @MethodSource("getObjectProvider")
    fun `success getObject`(inputAdapter: InputAdapter, context: HandlerContext, sendText: String) {
        val currentTestEntity = createCurrentTestEntity(gender = null, allowGender = AllowGender.FOR_ONE)

        val expected = createSendMessageAdapter(
            clearButtonsLater = true,
            chatId = inputAdapter.chatId,
            text = listOf(
                UnderlineSelection(text = "$sendText\n\n"),
                DefaultSelection(text = SendAddGenderHandler.SELECT_GENDER_TEXT),
            ),
            buttons = listOf(
                listOf(
                    ButtonAdapter(
                        text = SendAddGenderHandler.MALE_GENDER_TEXT,
                        button = GenderButton(gender = Gender.MALE).toBaseButton(),
                    ),
                    ButtonAdapter(
                        text = SendAddGenderHandler.FEMALE_GENDER_TEXT,
                        button = GenderButton(gender = Gender.FEMALE).toBaseButton(),
                    ),
                ),
                listOf(
                    ButtonAdapter.createExitTestButton(),
                ),
            ),
        )

        sendAddGenderHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 0) { inputHandler.getObject(any(), any(), any()) }
    }
}
