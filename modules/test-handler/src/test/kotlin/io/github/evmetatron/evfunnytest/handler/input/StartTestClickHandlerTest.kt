package io.github.evmetatron.evfunnytest.handler.input

import io.github.evmetatron.evfunnytest.dto.adapter.InputAdapter
import io.github.evmetatron.evfunnytest.dto.button.StartTestButton
import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import fixtures.createBaseButton
import fixtures.createCurrentTestEntity
import fixtures.createInputAdapter
import fixtures.createSendMessageAdapter
import fixtures.createTestEntity
import io.github.evmetatron.evfunnytest.service.CurrentTestService
import io.github.evmetatron.evfunnytest.service.TestService
import io.github.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.api.BeforeEach
import org.springframework.test.util.ReflectionTestUtils

@ExtendWith(MockKExtension::class)
internal class StartTestClickHandlerTest {
    @MockK
    private lateinit var testService: TestService

    @MockK
    private lateinit var currentTestService: CurrentTestService

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    private lateinit var startTestClickHandler: StartTestClickHandler

    @BeforeEach
    fun setUp() {
        ReflectionTestUtils.setField(startTestClickHandler, "inputHandler", inputHandler)
    }

    private companion object {
        @JvmStatic
        private fun verifyFalseProvider() =
            listOf(
                // Существует CurrentTestEntity
                Arguments.of(
                    createInputAdapter(
                        text = null,
                        button = createBaseButton(type = ButtonType.START_TEST),
                    ),
                    createCurrentTestEntity(),
                ),

                // Клик на кнопку не соответствует событию начала теста
                Arguments.of(
                    createInputAdapter(
                        text = null,
                        button = createBaseButton(type = ButtonType.PAGE),
                    ),
                    null,
                ),
            )
    }

    @ParameterizedTest
    @MethodSource("verifyFalseProvider")
    fun `verify false`(inputAdapter: InputAdapter, currentTestEntity: CurrentTestEntity?) {
        val sendMessage = createSendMessageAdapter()
        val context = HandlerContext()

        every { inputHandler.getObject(inputAdapter, currentTestEntity, context) } returns sendMessage

        startTestClickHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe sendMessage

        verify(exactly = 1) { inputHandler.getObject(inputAdapter, currentTestEntity, context) }
    }

    @Test
    fun `success getObject`() {
        val testId = 55L
        val inputAdapter = createInputAdapter(
            text = null,
            button = StartTestButton(testId = testId).toBaseButton()
        )
        val context = HandlerContext()
        val test = createTestEntity(id = testId)

        val currentTest = null

        val savedCurrentTest = createCurrentTestEntity(
            userId = inputAdapter.user.id,
            testId = testId,
            type = test.type,
            gender = null,
            allowGender = test.allowGender,
            answers = emptyList(),
        )

        every { testService.getTest(testId) } returns test

        val expected = createSendMessageAdapter()

        every { inputHandler.getObject(inputAdapter, savedCurrentTest, context.withHandledStart()) } returns expected
        every { currentTestService.createCurrentTest(inputAdapter.user.id, test) } returns savedCurrentTest

        startTestClickHandler.getObject(inputAdapter, currentTest, context) shouldBe expected

        verify(exactly = 1) { currentTestService.createCurrentTest(inputAdapter.user.id, test) }
    }

    @Test
    fun `fail getObject - test not found`() {
        val testId = 55L
        val inputAdapter = createInputAdapter(
            text = null,
            button = StartTestButton(testId = testId).toBaseButton()
        )
        val context = HandlerContext()

        val currentTestEntity = null

        every { testService.getTest(testId) } returns null

        val expected = inputAdapter.toSendMessage(GetTestClickHandler.TEST_NOT_FOUND)

        startTestClickHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 0) { inputHandler.getObject(any(), any(), any()) }
    }
}
