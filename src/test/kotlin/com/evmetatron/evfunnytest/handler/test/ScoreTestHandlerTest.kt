/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.test

import com.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.textselection.BoldSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.DefaultSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.ItalicSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.UnderlineSelection
import com.evmetatron.evfunnytest.dto.button.StartTestButton
import com.evmetatron.evfunnytest.dto.button.TestVariableButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.fixtures.createCurrentAnswerEntity
import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import com.evmetatron.evfunnytest.fixtures.createInputAdapter
import com.evmetatron.evfunnytest.fixtures.createQuestionScoreEntity
import com.evmetatron.evfunnytest.fixtures.createQuestionVariableScoreEntity
import com.evmetatron.evfunnytest.fixtures.createResultScoreEntity
import com.evmetatron.evfunnytest.fixtures.createSendMessageAdapter
import com.evmetatron.evfunnytest.fixtures.createTestScoreViewEntity
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.service.TestScoreService
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

@ExtendWith(MockKExtension::class)
internal class ScoreTestHandlerTest {
    @MockK
    private lateinit var testScoreService: TestScoreService

    @MockK(relaxed = true)
    private lateinit var currentTestService: CurrentTestService

    @MockK
    private lateinit var testHandler: TestHandler

    @InjectMockKs
    private lateinit var scoreTestHandler: ScoreTestHandler

    private companion object {
        @JvmStatic
        private fun successGetObjectProvider() =
            listOf(
                Arguments.of(
                    createInputAdapter(
                        text = null,
                        button = StartTestButton(testId = 1).toBaseButton(),
                    ),
                    HandlerContext().withHandledStart(),
                    AbstractTestHandler.STARTED_TEST_TEXT,
                ),

                Arguments.of(
                    createInputAdapter(
                        text = null,
                        button = StartTestButton(testId = 1).toBaseButton(),
                    ),
                    HandlerContext(),
                    ScoreTestHandler.ERROR_TEXT,
                ),
            )
    }

    @Test
    fun `verify false`() {
        val inputAdapter = createInputAdapter()
        val currentTestEntity = createCurrentTestEntity(
            type = TestType.REPLACE,
        )
        val context = HandlerContext()

        val sendMessage = createSendMessageAdapter()

        every { testHandler.getObject(inputAdapter, currentTestEntity, context) } returns sendMessage

        scoreTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe sendMessage

        verify(exactly = 1) { testHandler.getObject(inputAdapter, currentTestEntity, context) }
    }

    @Test
    fun `fail getObject - test not found`() {
        val inputAdapter = createInputAdapter()
        val currentTestEntity = createCurrentTestEntity(
            type = TestType.SCORE,
        )
        val context = HandlerContext()

        every { testScoreService.getTest(currentTestEntity.testId) } returns null

        scoreTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe
            inputAdapter.toSendMessageDefault(AbstractTestHandler.TEST_NOT_FOUND_TEXT)

        verify(exactly = 1) { currentTestService.removeCurrentTest(currentTestEntity.userId) }
    }

    @Suppress("LongMethod")
    @Test
    fun `success getObject - send result`() {
        val inputAdapter = createInputAdapter(
            text = null,
            button = TestVariableButton(variableId = 2).toBaseButton(),
            command = null,
        )
        val currentTestEntity = createCurrentTestEntity(
            userId = inputAdapter.user.id,
            type = TestType.SCORE,
            gender = null,
            answers = listOf(
                createCurrentAnswerEntity(num = 1, answer = "1"),
            ),
        )
        val context = HandlerContext()

        val test = createTestScoreViewEntity(
            questions = listOf(
                createQuestionScoreEntity(
                    num = 1,
                    question = "Question 1",
                    description = "Description 1",
                    variables = listOf(
                        createQuestionVariableScoreEntity(
                            id = 1,
                            variable = "Variable 1",
                            isTrue = false,
                        ),
                    ),
                ),
                createQuestionScoreEntity(
                    num = 2,
                    question = "Question 2",
                    description = "Description 2",
                    variables = listOf(
                        createQuestionVariableScoreEntity(
                            id = 2,
                            variable = "Variable 2",
                            isTrue = true,
                        ),
                    ),
                ),
            ),
            results = listOf(
                createResultScoreEntity(from = 1, to = 1, result = "Expected result"),
                createResultScoreEntity(from = 0, to = 0),
                createResultScoreEntity(from = 2, to = null),
            ),
        )

        val expected = inputAdapter.toSendMessage(
            listOf(
                BoldSelection(AbstractTestHandler.TEST_DONE_TEXT),
                DefaultSelection("\n\n"),
                DefaultSelection("Expected result"),
                DefaultSelection("\n\n\n\n"),
                BoldSelection(ScoreTestHandler.QUESTION_LABEL_TEXT),
                DefaultSelection("Question 1"),
                DefaultSelection("\n\n"),
                BoldSelection(ScoreTestHandler.ANSWER_LABEL_TEXT),
                DefaultSelection("Variable 1"),
                DefaultSelection("\n\n"),
                ItalicSelection(ScoreTestHandler.ANSWER_IS_FALSE_TEXT),
                ItalicSelection(" (Description 1)"),
                DefaultSelection("\n\n\n\n"),
                BoldSelection(ScoreTestHandler.QUESTION_LABEL_TEXT),
                DefaultSelection("Question 2"),
                DefaultSelection("\n\n"),
                BoldSelection(ScoreTestHandler.ANSWER_LABEL_TEXT),
                DefaultSelection("Variable 2"),
                DefaultSelection("\n\n"),
                ItalicSelection(ScoreTestHandler.ANSWER_IS_TRUE_TEXT),
                ItalicSelection(" (Description 2)"),
            ),
        )

        every { testScoreService.getTest(currentTestEntity.testId) } returns test

        scoreTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 1) { currentTestService.removeCurrentTest(currentTestEntity.userId) }
    }

    @Test
    fun `success getObject - save answer`() {
        val inputAdapter = createInputAdapter(
            text = null,
            button = TestVariableButton(variableId = 2).toBaseButton(),
            command = null,
        )
        val currentTestEntity = createCurrentTestEntity(
            userId = inputAdapter.user.id,
            type = TestType.SCORE,
            gender = null,
            answers = listOf(
                createCurrentAnswerEntity(num = 1, answer = "1"),
            ),
        )
        val context = HandlerContext()

        val variables = (1..3).map { createQuestionVariableScoreEntity() }

        val test = createTestScoreViewEntity(
            questions = listOf(
                createQuestionScoreEntity(num = 1),
                createQuestionScoreEntity(num = 2),
                createQuestionScoreEntity(
                    num = 3,
                    question = "Question 3",
                    variables = variables,
                ),
            ),
        )

        val replacedCurrentTest = currentTestEntity.withAnswer("2")

        every { testScoreService.getTest(currentTestEntity.testId) } returns test

        val expected = SendMessageAdapter(
            chatId = inputAdapter.chatId,
            clearButtonsLater = true,
            text = listOf(
                UnderlineSelection(AbstractTestHandler.ANSWER_ACCEPTED_TEXT),
                DefaultSelection("\n\n"),
                DefaultSelection("Question 3")
            ),
            buttons = variables.chunked(ScoreTestHandler.DEFAULT_CHUNK)
                .map { chunkedVariable ->
                    chunkedVariable.map { variable ->
                        ButtonAdapter(
                            text = variable.variable,
                            button = TestVariableButton(
                                variableId = variable.id,
                            ).toBaseButton(),
                        )
                    }
                } + listOf(
                listOf(
                    ButtonAdapter.createCancelAnswerButton(),
                    ButtonAdapter.createExitTestButton(),
                ),
            ),
        )

        scoreTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 1) { currentTestService.replaceCurrentTest(replacedCurrentTest) }
    }

    @ParameterizedTest
    @MethodSource("successGetObjectProvider")
    fun `success getObject - without save answer`(
        inputAdapter: InputAdapter,
        context: HandlerContext,
        addedMessage: String,
    ) {
        val currentTestEntity = createCurrentTestEntity(
            userId = inputAdapter.user.id,
            type = TestType.SCORE,
            gender = null,
            answers = listOf(
                createCurrentAnswerEntity(num = 1, answer = "1"),
            ),
        )

        val variables = (1..3).map { createQuestionVariableScoreEntity() }

        val test = createTestScoreViewEntity(
            questions = listOf(
                createQuestionScoreEntity(num = 1),
                createQuestionScoreEntity(
                    num = 2,
                    question = "Question 2",
                    variables = variables,
                ),
                createQuestionScoreEntity(num = 3),
            ),
        )

        every { testScoreService.getTest(currentTestEntity.testId) } returns test

        val expected = SendMessageAdapter(
            chatId = inputAdapter.chatId,
            clearButtonsLater = true,
            text = listOf(
                UnderlineSelection(addedMessage),
                DefaultSelection("\n\n"),
                DefaultSelection("Question 2")
            ),
            buttons = variables.chunked(ScoreTestHandler.DEFAULT_CHUNK)
                .map { chunkedVariable ->
                    chunkedVariable.map { variable ->
                        ButtonAdapter(
                            text = variable.variable,
                            button = TestVariableButton(
                                variableId = variable.id,
                            ).toBaseButton(),
                        )
                    }
                } + listOf(
                listOf(
                    ButtonAdapter.createCancelAnswerButton(),
                    ButtonAdapter.createExitTestButton(),
                ),
            ),
        )

        scoreTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 0) { currentTestService.replaceCurrentTest(any()) }
    }
}
