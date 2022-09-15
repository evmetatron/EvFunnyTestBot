/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.button.StartTestClick
import com.evmetatron.evfunnytest.dto.event.ClearButtons
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.exception.TestHandlerNotFoundException
import com.evmetatron.evfunnytest.handler.test.TestHandler
import com.evmetatron.evfunnytest.storage.db.repository.TestRepository
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.storage.memory.repository.CurrentTestRepository
import com.evmetatron.evfunnytest.utils.getButtonClick
import com.evmetatron.evfunnytest.utils.getChat
import com.evmetatron.evfunnytest.utils.getUser
import com.evmetatron.evfunnytest.utils.toSendMessage
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

class StartTestClickHandler(
    private val testRepository: TestRepository,
    private val currentTestRepository: CurrentTestRepository,
    private val testHandler: TestHandler?,
    private val publisher: ApplicationEventPublisher,
    inputHandler: InputHandler?,
) : AbstractInputHandler(inputHandler) {
    companion object {
        const val TEST_NOT_FOUND = "Тест не найден"
        const val ADDITIONAL_TEXT = "Запущен тест \"{test}\""
    }

    override fun verify(update: Update, currentTestEntity: CurrentTestEntity?): Boolean {
        val isEmptyCurrentTest = currentTestEntity == null
        val isStartTest = update.getButtonClick()?.let { it.type == ButtonType.START_TEST } ?: false

        return isEmptyCurrentTest && isStartTest
    }

    override fun handle(update: Update, currentTestEntity: CurrentTestEntity?): PartialBotApiMethod<*> {
        // Проверка на существование произведена в verify
        val startTestClick = update.getButtonClick()!!.let { StartTestClick.ofButtonClick(it) }

        if (testHandler == null) {
            throw TestHandlerNotFoundException()
        }

        val test = testRepository.findByIdOrNull(startTestClick.testId) ?: return update.toSendMessage(TEST_NOT_FOUND)

        val createdCurrentTest = CurrentTestEntity(
            userId = update.getUser().id,
            testId = test.id,
            type = test.type,
        )

        currentTestRepository.save(createdCurrentTest)

        publisher.publishEvent(
            ClearButtons(
                chatId = update.getChat().id,
                messageId = update.callbackQuery.message.messageId,
            ),
        )

        return testHandler.getObject(update, createdCurrentTest, ADDITIONAL_TEXT.replace("{test}", test.name))
            ?: throw TestHandlerNotFoundException()
    }
}
