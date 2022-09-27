/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.test

import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import com.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

abstract class AbstractTestHandler(
    protected val currentTestService: CurrentTestService,
    private val testHandler: TestHandler?,
) : TestHandler {
    companion object {
        const val TEST_NOT_FOUND_TEXT = "Тест не найден и принудительно завершен"
        const val TEST_DONE_TEXT = "Тест завершен"
        const val ANSWER_CANCELED_TEXT = "Ответ отменен"
        const val ANSWER_ACCEPTED_TEXT = "Ответ принят"
        const val STARTED_TEST_TEXT = "Тест запущен"
    }
    override fun getObject(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity,
        context: HandlerContext,
    ): MessageAdapter? {
        if (currentTestEntity.type != testType()) {
            return testHandler?.getObject(inputAdapter, currentTestEntity, context)
        }

        return handle(inputAdapter, currentTestEntity, context)
    }

    protected fun abortTest(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity,
    ): SendMessageAdapter {
        currentTestService.removeCurrentTest(currentTestEntity.userId)

        return inputAdapter.toSendMessageDefault(TEST_NOT_FOUND_TEXT)
    }

    protected fun getAddedMessage(
        inputAdapter: InputAdapter,
        context: HandlerContext,
    ): String? {
        val isGenderButton = inputAdapter.button?.type == ButtonType.SELECT_GENDER
        val isCancelAnswerButton = inputAdapter.button?.type == ButtonType.CANCEL_ANSWER
        val isStart = context.isHandledStart()

        return when (true) {
            isGenderButton -> ANSWER_ACCEPTED_TEXT
            isCancelAnswerButton -> ANSWER_CANCELED_TEXT
            isStart -> STARTED_TEST_TEXT
            else -> null
        }
    }

    protected abstract fun testType(): TestType

    protected abstract fun handle(
        inputAdapter: InputAdapter,
        currentTestEntity: CurrentTestEntity,
        context: HandlerContext,
    ): MessageAdapter
}
