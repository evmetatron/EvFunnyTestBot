/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.test

import com.evmetatron.evfunnytest.dto.adapter.ButtonAdapter
import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.textselection.BoldSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.DefaultSelection
import com.evmetatron.evfunnytest.dto.adapter.textselection.UnderlineSelection
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.AllowGender
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.service.TestReplaceService
import com.evmetatron.evfunnytest.storage.db.entity.TestReplaceViewEntity
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

class ReplaceTestHandler(
    private val testReplaceService: TestReplaceService,
    private val currentTestService: CurrentTestService,
    testHandler: TestHandler?,
) : AbstractTestHandler(testHandler) {
    companion object {
        const val TEST_NOT_FOUND_TEXT = "Тест не найден и принудительно завершен"
        const val TEST_DONE_TEXT = "Тест завершен"
        const val ANSWER_TO_QUESTION_TEXT = "Ответь на вопрос"
        const val ANSWER_ACCEPTED_TEXT = "Ответ принят"
        const val ANSWER_CANCELED_TEXT = "Ответ отменен"
        const val STARTED_TEST_TEXT = "Тест запущен"
        const val ERROR_TEXT = "Во время прохождения теста команды и клики по кнопкам " +
            "кроме отмены ответа или завершения теста не работают"
    }

    override fun testType(): TestType =
        TestType.REPLACE

    override fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity,
        context: HandlerContext,
    ): MessageAdapter {
        val test = testReplaceService.getTest(currentTestEntity.testId)

        if (test == null) {
            currentTestService.removeCurrentTest(currentTestEntity.userId)

            return inputAdapter.toSendMessageDefault(TEST_NOT_FOUND_TEXT)
        }

        val isMessageOnly = inputAdapter.isMessageOnly()

        val replacedCurrentTest = isMessageOnly.takeIf { it }
            ?.let {
                currentTestEntity.withAnswer(inputAdapter.text!!)
            } ?: currentTestEntity

        return getMessage(replacedCurrentTest, test, inputAdapter, context, isMessageOnly)
    }

    private fun getMessage(
        replacedCurrentTest: CurrentTestEntity,
        test: TestReplaceViewEntity,
        inputAdapter: InputAdapter,
        context: HandlerContext,
        isMessageOnly: Boolean,
    ): SendMessageAdapter {
        if (replacedCurrentTest.answers.size >= test.questions.size) {
            var result = if (replacedCurrentTest.allowGender == AllowGender.ALL) {
                test.results.first().result
            } else {
                test.results.first { it.gender == replacedCurrentTest.gender }.result
            }

            replacedCurrentTest.answers.forEach { question ->
                val num = question.num
                result = result.replace("{num$num}", question.answer)
            }

            currentTestService.removeCurrentTest(replacedCurrentTest.userId)

            return inputAdapter.toSendMessage(
                BoldSelection(TEST_DONE_TEXT),
                DefaultSelection("\n\n"),
                DefaultSelection(result),
            )
        }

        if (isMessageOnly) {
            currentTestService.replaceCurrentTest(replacedCurrentTest)
        }

        val answerNum = replacedCurrentTest.getNeedAnswerNum()

        val isGenderButton = inputAdapter.button?.type == ButtonType.SELECT_GENDER
        val isCancelAnswerButton = inputAdapter.button?.type == ButtonType.CANCEL_ANSWER
        val isStart = context.isHandledStart()

        val addedMessage = getAddedMessage(isGenderButton, isMessageOnly, isCancelAnswerButton, isStart)

        return SendMessageAdapter(
            chatId = inputAdapter.chatId,
            clearButtonsLater = true,
            text = listOf(
                UnderlineSelection(addedMessage),
                DefaultSelection("\n\n"),
                BoldSelection(ANSWER_TO_QUESTION_TEXT),
                DefaultSelection("\n\n"),
                DefaultSelection(test.questions.first { it.num == answerNum }.question)
            ),
            buttons = listOf(
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

    private fun getAddedMessage(
        isGenderButton: Boolean,
        isMessageOnly: Boolean,
        isCancelAnswerButton: Boolean,
        isStart: Boolean,
    ): String = when (true) {
        (isGenderButton || isMessageOnly) -> ANSWER_ACCEPTED_TEXT
        isCancelAnswerButton -> ANSWER_CANCELED_TEXT
        isStart -> STARTED_TEST_TEXT
        else -> ERROR_TEXT
    }
}
