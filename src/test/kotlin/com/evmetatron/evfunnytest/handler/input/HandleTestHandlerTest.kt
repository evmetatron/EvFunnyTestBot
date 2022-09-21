/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.AllowGender
import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import com.evmetatron.evfunnytest.fixtures.createInputAdapter
import com.evmetatron.evfunnytest.fixtures.createSendMessageAdapter
import com.evmetatron.evfunnytest.handler.test.TestHandler
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
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
internal class HandleTestHandlerTest {
    @MockK
    private lateinit var testHandler: TestHandler

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    private lateinit var handleTestHandler: HandleTestHandler

    private companion object {
        @JvmStatic
        private fun verifyFalseProvider() =
            listOf(
                // CurrentTestEntity не создан
                Arguments.of(
                    null,
                ),

                // Пол не указан, но он должен быть указан
                Arguments.of(
                    createCurrentTestEntity(
                        allowGender = AllowGender.FOR_ONE,
                        gender = null,
                    ),
                ),
            )
    }

    @ParameterizedTest
    @MethodSource("verifyFalseProvider")
    fun `verify false`(currentTestEntity: CurrentTestEntity?) {
        val inputAdapter = createInputAdapter()
        val sendMessage = createSendMessageAdapter()
        val context = HandlerContext()

        every { inputHandler.getObject(inputAdapter, currentTestEntity, context) } returns sendMessage

        handleTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe sendMessage

        verify(exactly = 1) { inputHandler.getObject(inputAdapter, currentTestEntity, context) }
    }

    @Test
    fun `success getObject`() {
        val inputAdapter = createInputAdapter()
        val currentTestEntity = createCurrentTestEntity(
            allowGender = AllowGender.ALL,
        )
        val context = HandlerContext()

        val expected = createSendMessageAdapter()

        every { testHandler.getObject(inputAdapter, currentTestEntity) } returns expected

        handleTestHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 0) { inputHandler.getObject(any(), any(), any()) }
    }
}
