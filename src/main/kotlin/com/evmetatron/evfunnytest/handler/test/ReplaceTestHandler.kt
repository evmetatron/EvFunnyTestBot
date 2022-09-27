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
import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.service.TestReplaceService
import com.evmetatron.evfunnytest.storage.db.entity.TestReplaceViewEntity
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

class ReplaceTestHandler(
    private val testReplaceService: TestReplaceService,
    currentTestService: CurrentTestService,
    testHandler: TestHandler?,
) : AbstractTestHandler(currentTestService, testHandler) {
    companion object {
        const val ANSWER_TO_QUESTION_TEXT = "Ответь на вопрос"
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
            ?: return abortTest(inputAdapter, currentTestEntity)

        val replacedCurrentTest = inputAdapter.isMessageOnly().takeIf { it }
            ?.let {
                currentTestEntity.withAnswer(inputAdapter.text!!)
            } ?: currentTestEntity

        return getMessage(replacedCurrentTest, test, inputAdapter, context)
    }

    private fun getMessage(
        replacedCurrentTest: CurrentTestEntity,
        test: TestReplaceViewEntity,
        inputAdapter: InputAdapter,
        context: HandlerContext,
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

        val isMessageOnly = inputAdapter.isMessageOnly()

        if (inputAdapter.isMessageOnly()) {
            currentTestService.replaceCurrentTest(replacedCurrentTest)
        }

        val answerNum = replacedCurrentTest.getNeedAnswerNum()

        val addedMessage = getAddedMessage(inputAdapter, context)
            ?: isMessageOnly.takeIf { it }?.let { ANSWER_ACCEPTED_TEXT }
            ?: ERROR_TEXT

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
}
