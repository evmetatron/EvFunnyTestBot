/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.test

import com.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import com.evmetatron.evfunnytest.dto.button.TestVariableButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.service.TestScoreService
import com.evmetatron.evfunnytest.storage.db.entity.TestScoreViewEntity
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

class ScoreTestHandler(
    private val testScoreService: TestScoreService,
    currentTestService: CurrentTestService,
    testHandler: TestHandler?,
) : AbstractTestHandler(currentTestService, testHandler) {
    companion object {
        const val DEFAULT_CHUNK = 2
        const val QUESTION_LABEL_TEXT = "Вопрос: "
        const val ANSWER_LABEL_TEXT = "Ваш ответ: "
        const val ANSWER_IS_TRUE_TEXT = "Ответ правильный"
        const val ANSWER_IS_FALSE_TEXT = "Ответ не правильный"
        const val ERROR_TEXT = "Во время прохождения теста работают только кнопки под вопросом " +
            "и команда отмены"
    }

    override fun testType(): TestType =
        TestType.SCORE

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity,
        context: HandlerContext
    ): MessageAdapter {
        val test = testScoreService.getTest(currentTestEntity.testId)
            ?: return abortTest(inputAdapter, currentTestEntity)

        return sendMessage(inputAdapter, currentTestEntity, test, context)
    }

    private fun sendMessage(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity,
        test: TestScoreViewEntity,
        context: HandlerContext
    ): SendMessageAdapter {
        val button = inputAdapter.button?.toConcreteButton()

        val replacedCurrentTest = if (button is TestVariableButton) {
            currentTestEntity.withAnswer(button.variableId.toString())
        } else {
            currentTestEntity
        }

        if (replacedCurrentTest.answers.size >= test.questions.size) {
            return sendResult(test, replacedCurrentTest, inputAdapter)
        }

        if (button is TestVariableButton) {
            currentTestService.replaceCurrentTest(replacedCurrentTest)
        }

        val answerNum = replacedCurrentTest.getNeedAnswerNum()

        val addedMessage = getAddedMessage(inputAdapter, context)
            ?: (button is TestVariableButton).takeIf { it }?.let { ANSWER_ACCEPTED_TEXT }
            ?: ERROR_TEXT

        val question = test.questions.first { it.num == answerNum }

        return SendMessageAdapter(
            chatId = inputAdapter.chatId,
            clearButtonsLater = true,
            text = "[u]$addedMessage[/u]\n\n${question.question}",
            buttons = question.variables.chunked(DEFAULT_CHUNK)
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
                listOfNotNull(
                    (replacedCurrentTest.answers.isNotEmpty() || replacedCurrentTest.gender != null)
                        .takeIf { it }
                        ?.let {
                            ButtonAdapter.createCancelAnswerButton()
                        },
                    ButtonAdapter.createExitTestButton(),
                ),
            ),
        )
    }

    private fun sendResult(
        test: TestScoreViewEntity,
        replacedCurrentTest: CurrentTestEntity,
        inputAdapter: InputAdapter,
    ): SendMessageAdapter {
        val mappedTest = test.questions.associateBy { it.num }

        val mappedAnswers = replacedCurrentTest.answers.map { currentTest ->
            currentTest to mappedTest.getValue(currentTest.num)
        }

        val score = mappedAnswers.count { (answer, question) ->
            question.variables.first { it.id == answer.answer.toLong() }.isTrue
        }

        val result = test.results.first { result ->
            score >= result.from && score <= (result.to ?: Int.MAX_VALUE)
        }

        val answerResults = mappedAnswers.joinToString(separator = "") { (answer, question) ->
            val yourAnswer = question.variables.first { it.id == answer.answer.toLong() }

            val answerText = yourAnswer.isTrue.takeIf { it }?.let { ANSWER_IS_TRUE_TEXT }
                ?: ANSWER_IS_FALSE_TEXT

            "\n\n\n\n[b]$QUESTION_LABEL_TEXT[/b]${question.question}\n\n[b]$ANSWER_LABEL_TEXT[/b]" +
                "${yourAnswer.variable}\n\n[i]$answerText (${question.description})[/i]"
        }

        currentTestService.removeCurrentTest(replacedCurrentTest.userId)

        return inputAdapter.toSendMessage("[b]$TEST_DONE_TEXT[/b]\n\n${result.result}$answerResults")
    }
}
