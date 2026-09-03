package io.github.evmetatron.evfunnytest.handler.test

import io.github.evmetatron.evfunnytest.dto.adapter.InputAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.MessageAdapter
import io.github.evmetatron.evfunnytest.dto.adapter.SendMessageAdapter
import io.github.evmetatron.evfunnytest.dto.context.HandlerContext
import io.github.evmetatron.evfunnytest.enumerable.ButtonType
import io.github.evmetatron.evfunnytest.enumerable.TestType
import io.github.evmetatron.evfunnytest.service.CurrentTestService
import io.github.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import io.github.evmetatron.spring.cor.ChainNext

abstract class AbstractTestHandler(
    protected val currentTestService: CurrentTestService,
) : TestHandler {
    @ChainNext
    private lateinit var testHandler: TestHandler

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

        return inputAdapter.toSendMessage(TEST_NOT_FOUND_TEXT)
    }

    protected fun getAddedMessage(
        inputAdapter: InputAdapter,
        context: HandlerContext,
    ): String? {
        val isGenderButton = inputAdapter.button?.type == ButtonType.SELECT_GENDER
        val isCancelAnswerButton = inputAdapter.button?.type == ButtonType.CANCEL_ANSWER
        val isStart = context.isHandledStart()

        return when {
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
