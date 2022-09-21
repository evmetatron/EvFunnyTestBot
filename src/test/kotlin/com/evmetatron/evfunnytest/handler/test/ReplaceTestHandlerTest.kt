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
import com.evmetatron.evfunnytest.dto.adapter.textselection.UnderlineSelection
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.enumerable.Gender
import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.fixtures.createBaseButton
import com.evmetatron.evfunnytest.fixtures.createCurrentAnswerEntity
import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import com.evmetatron.evfunnytest.fixtures.createInputAdapter
import com.evmetatron.evfunnytest.fixtures.createQuestionReplaceEntity
import com.evmetatron.evfunnytest.fixtures.createResultReplaceEntity
import com.evmetatron.evfunnytest.fixtures.createSendMessageAdapter
import com.evmetatron.evfunnytest.fixtures.createTestReplaceViewEntity
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.service.TestReplaceService
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
internal class ReplaceTestHandlerTest {
    @MockK
    private lateinit var testReplaceService: TestReplaceService

    @MockK(relaxed = true)
    private lateinit var currentTestService: CurrentTestService

    @MockK
    private lateinit var testHandler: TestHandler

    @InjectMockKs
    private lateinit var replaceTestHandler: ReplaceTestHandler

    private companion object {
        @JvmStatic
        private fun successGetObjectProvider() =
            listOf(
                Arguments.of(
                    createInputAdapter(
                        text = null,
                        button = createBaseButton(type = ButtonType.START_TEST),
                    ),
                    HandlerContext().withHandledStart(),
                    ReplaceTestHandler.STARTED_TEST_TEXT,
                ),

                Arguments.of(
                    createInputAdapter(
                        text = null,
                        button = createBaseButton(type = ButtonType.SELECT_GENDER),
                    ),
                    HandlerContext(),
                    ReplaceTestHandler.ANSWER_ACCEPTED_TEXT,
                ),

                Arguments.of(
                    createInputAdapter(
                        text = null,
                        button = createBaseButton(type = ButtonType.START_TEST),
                    ),
                    HandlerContext(),
                    ReplaceTestHandler.ERROR_TEXT,
                ),
            )
    }

    @Test
    fun `verify false`() {
        val inputAdapter = createInputAdapter()
        val currentTestEntity = createCurrentTestEntity(
            type = TestType.SCORE,
        )
        val context = HandlerContext()

        val sendMessage = createSendMessageAdapter()

        every { testHandler.getObject(inputAdapter, currentTestEntity, context) } returns sendMessage

        replaceTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe sendMessage

        verify(exactly = 1) { testHandler.getObject(inputAdapter, currentTestEntity, context) }
    }

    @Test
    fun `fail getObject - test not found`() {
        val inputAdapter = createInputAdapter()
        val currentTestEntity = createCurrentTestEntity(
            type = TestType.REPLACE,
        )
        val context = HandlerContext()

        every { testReplaceService.getTest(currentTestEntity.testId) } returns null

        replaceTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe
            inputAdapter.toSendMessageDefault(ReplaceTestHandler.TEST_NOT_FOUND_TEXT)

        verify(exactly = 1) { currentTestService.removeCurrentTest(currentTestEntity.userId) }
    }

    @Test
    fun `success getObject - send result`() {
        val inputAdapter = createInputAdapter(
            text = "Ответ А",
            button = null,
            command = null,
        )
        val currentTestEntity = createCurrentTestEntity(
            userId = inputAdapter.user.id,
            type = TestType.REPLACE,
            gender = Gender.MALE,
            answers = listOf(
                createCurrentAnswerEntity(num = 1, answer = "Ответ Б"),
                createCurrentAnswerEntity(num = 2, answer = "Ответ В"),
                createCurrentAnswerEntity(num = 3, answer = "Ответ Г"),
                createCurrentAnswerEntity(num = 4, answer = "Ответ Д"),
            ),
        )
        val context = HandlerContext()

        val test = createTestReplaceViewEntity(
            questions = listOf(
                createQuestionReplaceEntity(num = 1),
                createQuestionReplaceEntity(num = 2),
                createQuestionReplaceEntity(num = 3),
                createQuestionReplaceEntity(num = 4),
                createQuestionReplaceEntity(num = 5),
            ),
            results = listOf(
                createResultReplaceEntity(
                    gender = Gender.MALE,
                    result = "вот {num1} и {num2} так {num1} без {num3} в {num4} над {num5}",
                ),
                createResultReplaceEntity(gender = Gender.MALE),
            ),
        )

        val expected = inputAdapter.toSendMessage(
            BoldSelection(ReplaceTestHandler.TEST_DONE_TEXT),
            DefaultSelection("\n\n"),
            DefaultSelection("вот Ответ Б и Ответ В так Ответ Б без Ответ Г в Ответ Д над Ответ А"),
        )

        every { testReplaceService.getTest(currentTestEntity.testId) } returns test

        replaceTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 1) { currentTestService.removeCurrentTest(currentTestEntity.userId) }
    }

    @Test
    fun `success getObject - save answer`() {
        val inputAdapter = createInputAdapter(
            text = "Ответ А",
            button = null,
            command = null,
        )
        val currentTestEntity = createCurrentTestEntity(
            userId = inputAdapter.user.id,
            type = TestType.REPLACE,
            answers = listOf(
                createCurrentAnswerEntity(num = 1),
            ),
        )

        val replacedCurrentTest = currentTestEntity.withAnswer(inputAdapter.text!!)

        val context = HandlerContext()

        val test = createTestReplaceViewEntity(
            questions = listOf(
                createQuestionReplaceEntity(num = 1),
                createQuestionReplaceEntity(num = 2),
                createQuestionReplaceEntity(num = 3),
            ),
        )

        every { testReplaceService.getTest(currentTestEntity.testId) } returns test

        val answerNum = replacedCurrentTest.getNeedAnswerNum()

        val expected = SendMessageAdapter(
            chatId = inputAdapter.chatId,
            clearButtonsLater = true,
            text = listOf(
                UnderlineSelection(ReplaceTestHandler.ANSWER_ACCEPTED_TEXT),
                DefaultSelection("\n\n"),
                BoldSelection(ReplaceTestHandler.ANSWER_TO_QUESTION_TEXT),
                DefaultSelection("\n\n"),
                DefaultSelection(test.questions.first { it.num == answerNum }.question)
            ),
            buttons = listOf(
                listOfNotNull(
                    ButtonAdapter.createCancelAnswerButton(),
                    ButtonAdapter.createExitTestButton(),
                ),
            ),
        )

        replaceTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

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
            type = TestType.REPLACE,
            answers = listOf(
                createCurrentAnswerEntity(num = 1),
            ),
        )

        val test = createTestReplaceViewEntity(
            questions = listOf(
                createQuestionReplaceEntity(num = 1),
                createQuestionReplaceEntity(num = 2),
                createQuestionReplaceEntity(num = 3),
            ),
        )

        every { testReplaceService.getTest(currentTestEntity.testId) } returns test

        val answerNum = currentTestEntity.getNeedAnswerNum()

        val expected = SendMessageAdapter(
            chatId = inputAdapter.chatId,
            clearButtonsLater = true,
            text = listOf(
                UnderlineSelection(addedMessage),
                DefaultSelection("\n\n"),
                BoldSelection(ReplaceTestHandler.ANSWER_TO_QUESTION_TEXT),
                DefaultSelection("\n\n"),
                DefaultSelection(test.questions.first { it.num == answerNum }.question)
            ),
            buttons = listOf(
                listOfNotNull(
                    ButtonAdapter.createCancelAnswerButton(),
                    ButtonAdapter.createExitTestButton(),
                ),
            ),
        )

        replaceTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 0) { currentTestService.replaceCurrentTest(any()) }
    }
}
