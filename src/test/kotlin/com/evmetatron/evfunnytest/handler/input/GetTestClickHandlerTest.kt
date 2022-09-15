/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.textselection.BoldSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.DefaultSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.ItalicSelection
import com.evmetatron.evfunnytest.dto.button.GetTestButton
import com.evmetatron.evfunnytest.dto.button.StartTestButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.fixtures.createBaseButton
import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import com.evmetatron.evfunnytest.fixtures.createInputAdapter
import com.evmetatron.evfunnytest.fixtures.createSendMessageAdapter
import com.evmetatron.evfunnytest.fixtures.createTestEntity
import com.evmetatron.evfunnytest.service.TestService
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class GetTestClickHandlerTest {
    @MockK
    private lateinit var testService: TestService

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    private lateinit var getTestClickHandler: GetTestClickHandler

    @ParameterizedTest
    @MethodSource("verifyFalseProvider")
    fun `verify false`(inputAdapter: InputAdapter, currentTestEntity: CurrentTestEntity?) {
        val sendMessage = createSendMessageAdapter()
        val context = HandlerContext()

        every { inputHandler.getObject(inputAdapter, currentTestEntity, context) } returns sendMessage

        getTestClickHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe sendMessage

        verify(exactly = 1) { inputHandler.getObject(inputAdapter, currentTestEntity, context) }
    }

    @Test
    fun `success getObject`() {
        val testId = 55L
        val inputAdapter = createInputAdapter(
            text = null,
            button = GetTestButton(testId = testId).toBaseButton()
        )
        val context = HandlerContext()

        val test = createTestEntity()

        val currentTestEntity = null

        every {
            testService.getTest(testId)
        } returns test

        val text = listOf(
            BoldSelection(test.name),
            DefaultSelection("\n\n"),
            ItalicSelection(test.description),
        )

        val expected = createSendMessageAdapter(
            chatId = inputAdapter.chatId,
            text = text,
            buttons = listOf(
                listOf(
                    ButtonAdapter(
                        text = GetTestClickHandler.BUTTON_TEXT,
                        button = StartTestButton(
                            testId = test.id,
                        ).toBaseButton(),
                    ),
                ),
            ),
        )

        getTestClickHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 0) { inputHandler.getObject(inputAdapter, currentTestEntity, context) }
    }

    @Test
    fun `fail getObject - test not found`() {
        val testId = 55L
        val inputAdapter = createInputAdapter(
            text = null,
            button = GetTestButton(testId = testId).toBaseButton()
        )
        val context = HandlerContext()

        val currentTestEntity = null

        every {
            testService.getTest(testId)
        } returns null

        val expected = inputAdapter.toSendMessageDefault(GetTestClickHandler.TEST_NOT_FOUND)

        getTestClickHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 0) { inputHandler.getObject(inputAdapter, currentTestEntity, context) }
    }

    private fun verifyFalseProvider() =
        listOf(
            // Существует CurrentTestEntity
            Arguments.of(
                createInputAdapter(
                    text = null,
                    button = createBaseButton(type = ButtonType.GET_TEST),
                ),
                createCurrentTestEntity(),
            ),
            // Клик на кнопку не соответствует событию просмотра теста
            Arguments.of(
                createInputAdapter(
                    text = null,
                    button = createBaseButton(type = ButtonType.PAGE),
                ),
                null,
            ),
        )
}
