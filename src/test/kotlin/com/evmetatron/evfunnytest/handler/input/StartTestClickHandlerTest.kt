/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.button.ButtonClick
import com.evmetatron.evfunnytest.dto.button.StartTestClick
import com.evmetatron.evfunnytest.dto.event.ClearButtons
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.fixtures.createCallbackQuery
import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import com.evmetatron.evfunnytest.fixtures.createTestEntity
import com.evmetatron.evfunnytest.fixtures.createUpdate
import com.evmetatron.evfunnytest.fixtures.faker
import com.evmetatron.evfunnytest.handler.test.TestHandler
import com.evmetatron.evfunnytest.storage.db.repository.TestRepository
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.storage.memory.repository.CurrentTestRepository
import com.evmetatron.evfunnytest.utils.getChat
import com.evmetatron.evfunnytest.utils.getUser
import com.evmetatron.evfunnytest.utils.toSendMessage
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
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class StartTestClickHandlerTest {
    @MockK
    private lateinit var testRepository: TestRepository

    @MockK
    private lateinit var currentTestRepository: CurrentTestRepository

    @MockK
    private lateinit var testHandler: TestHandler

    @MockK(relaxed = true)
    private lateinit var publisher: ApplicationEventPublisher

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    private lateinit var startTestClickHandler: StartTestClickHandler

    @ParameterizedTest
    @MethodSource("verifyFalseProvider")
    fun `verify false`(update: Update, currentTestEntity: CurrentTestEntity?) {
        val sendMessage = SendMessage().apply {
            text = faker.harryPotter().quote()
        }

        every { inputHandler.getObject(update, currentTestEntity) } returns sendMessage

        startTestClickHandler.getObject(update, currentTestEntity) shouldBe sendMessage

        verify(exactly = 1) { inputHandler.getObject(update, currentTestEntity) }
    }

    @Test
    fun `success getObject`() {
        val testId = 55L
        val update = createUpdate(
            message = null,
            callbackQuery = createCallbackQuery(
                data = StartTestClick(testId = testId).toButtonClick().toJson(),
            ),
        )
        val test = createTestEntity(id = testId)

        val additionalText = StartTestClickHandler.ADDITIONAL_TEXT.replace("{test}", test.name)

        val currentTestEntity = null

        val currentTest = createCurrentTestEntity(
            userId = update.getUser().id,
            testId = testId,
            type = test.type,
            answers = emptyList(),
        )

        val clearButtons = ClearButtons(
            chatId = update.getChat().id,
            messageId = update.callbackQuery.message.messageId,
        )

        every {
            testRepository.findByIdOrNull(testId)
        } returns test

        val expected = SendMessage().apply {
            this.text = faker.harryPotter().quote()
        }

        every { testHandler.getObject(update, currentTest, additionalText) } returns expected
        every { currentTestRepository.save(currentTest) } returns currentTest

        startTestClickHandler.getObject(update, currentTestEntity) shouldBe expected

        verify(exactly = 0) { inputHandler.getObject(update, currentTestEntity) }
        verify(exactly = 1) { currentTestRepository.save(currentTest) }
        verify(exactly = 1) { publisher.publishEvent(clearButtons) }
    }

    @Test
    fun `fail getObject - test not found`() {
        val testId = 55L
        val update = createUpdate(
            message = null,
            callbackQuery = createCallbackQuery(
                data = StartTestClick(testId = testId).toButtonClick().toJson(),
            ),
        )

        val currentTestEntity = null

        every {
            testRepository.findByIdOrNull(testId)
        } returns null

        val expected = update.toSendMessage(GetTestClickHandler.TEST_NOT_FOUND)

        startTestClickHandler.getObject(update, currentTestEntity) shouldBe expected

        verify(exactly = 0) { inputHandler.getObject(update, currentTestEntity) }
    }

    private fun verifyFalseProvider() =
        listOf(
            // Существует CurrentTestEntity
            Arguments.of(
                createUpdate(
                    message = null,
                    callbackQuery = createCallbackQuery(
                        data = ButtonClick(type = ButtonType.START_TEST, data = emptyMap()).toJson(),
                    ),
                ),
                createCurrentTestEntity(),
            ),
            // Клик на кнопку не соответствует событию начала теста
            Arguments.of(
                createUpdate(
                    message = null,
                    callbackQuery = createCallbackQuery(
                        data = ButtonClick(type = ButtonType.PAGE, data = emptyMap()).toJson()
                    )
                ),
                null,
            ),
        )
}
