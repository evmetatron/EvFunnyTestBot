/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.BotCommand
import com.evmetatron.evfunnytest.enumerable.ButtonType
import fixtures.createBaseButton
import fixtures.createCurrentTestEntity
import fixtures.createInputAdapter
import fixtures.createSendMessageAdapter
import fixtures.createTestEntity
import com.evmetatron.evfunnytest.service.CurrentTestService
import com.evmetatron.evfunnytest.service.TestService
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ExitTestHandlerTest {
    @MockK
    private lateinit var testService: TestService

    @MockK(relaxed = true)
    private lateinit var currentTestService: CurrentTestService

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    private lateinit var exitTestHandler: ExitTestHandler

    @Test
    fun `verify false`() {
        // Кнопка и команда несоответствуют тесту
        val inputAdapter = createInputAdapter(
            command = BotCommand.START,
            button = createBaseButton(type = ButtonType.START_TEST),
        )

        val currentTestEntity = createCurrentTestEntity()

        val sendMessage = createSendMessageAdapter()
        val context = HandlerContext()

        every { inputHandler.getObject(inputAdapter, currentTestEntity, context) } returns sendMessage

        exitTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe sendMessage

        verify(exactly = 1) { inputHandler.getObject(inputAdapter, currentTestEntity, context) }
    }

    @Test
    fun `failed getObject - current test not started`() {
        val inputAdapter = createInputAdapter(
            command = BotCommand.EXIT,
            button = null,
        )
        val currentTestEntity = null
        val context = HandlerContext()

        val expected = inputAdapter.toSendMessage(ExitTestHandler.TEST_NOT_STARTED_TEXT)

        exitTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 0) { inputHandler.getObject(any(), any(), any()) }
    }

    @Test
    fun `success getObject`() {
        val inputAdapter = createInputAdapter(
            command = null,
            button = createBaseButton(type = ButtonType.EXIT_TEST),
        )
        val currentTestEntity = createCurrentTestEntity()
        val context = HandlerContext()

        val testEntity = createTestEntity(id = currentTestEntity.testId)

        every { testService.getTest(currentTestEntity.testId) } returns testEntity

        val expected = inputAdapter.toSendMessage(
            ExitTestHandler.TEST_EXIT_TEXT.replace("{test}", testEntity.name),
        )

        exitTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 1) { currentTestService.removeCurrentTest(currentTestEntity.userId) }

        verify(exactly = 0) { inputHandler.getObject(any(), any(), any()) }
    }
}
