/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.handler.input

import com.evmetatron.evfunnytest.dto.adapter.InputAdapter
import com.evmetatron.evfunnytest.dto.button.GenderButton
import com.evmetatron.evfunnytest.dto.context.HandlerContext
import com.evmetatron.evfunnytest.enumerable.ButtonType
import com.evmetatron.evfunnytest.enumerable.Gender
import com.evmetatron.evfunnytest.fixtures.createBaseButton
import com.evmetatron.evfunnytest.fixtures.createCurrentTestEntity
import com.evmetatron.evfunnytest.fixtures.createInputAdapter
import com.evmetatron.evfunnytest.fixtures.createSendMessageAdapter
import com.evmetatron.evfunnytest.service.CurrentTestService
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
internal class GenderClickHandlerTest {
    @MockK(relaxed = true)
    private lateinit var currentTestService: CurrentTestService

    @MockK
    private lateinit var inputHandler: InputHandler

    @InjectMockKs
    private lateinit var genderClickHandler: GenderClickHandler

    private companion object {
        @JvmStatic
        private fun verifyFalseProvider() =
            listOf(
                // CurrentTestEntity не создан
                Arguments.of(
                    createInputAdapter(
                        button = createBaseButton(type = ButtonType.SELECT_GENDER),
                    ),
                    null,
                ),

                // Кнопка не соответствует кнопке выбора пола
                Arguments.of(
                    createInputAdapter(
                        button = createBaseButton(type = ButtonType.EXIT_TEST),
                    ),
                    createCurrentTestEntity(),
                ),
            )
    }

    @ParameterizedTest
    @MethodSource("verifyFalseProvider")
    fun `verify false`(inputAdapter: InputAdapter, currentTestEntity: CurrentTestEntity?) {
        val sendMessage = createSendMessageAdapter()
        val context = HandlerContext()

        every { inputHandler.getObject(inputAdapter, currentTestEntity, context) } returns sendMessage

        genderClickHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe sendMessage

        verify(exactly = 0) { currentTestService.replaceCurrentTest(any()) }

        verify(exactly = 1) { inputHandler.getObject(inputAdapter, currentTestEntity, context) }
    }

    @Test
    fun `success getObject`() {
        val button = GenderButton(gender = Gender.MALE)
        val inputAdapter = createInputAdapter(
            button = button.toBaseButton(),
        )
        val currentTestEntity = createCurrentTestEntity(userId = inputAdapter.user.id)
        val context = HandlerContext()

        val expected = createSendMessageAdapter()

        every { inputHandler.getObject(inputAdapter, currentTestEntity, context) } returns expected

        genderClickHandler.getObject(inputAdapter, currentTestEntity, context) shouldBe expected

        verify(exactly = 1) { currentTestService.replaceCurrentTest(currentTestEntity.withGender(button.gender)) }

        verify(exactly = 1) { inputHandler.getObject(inputAdapter, currentTestEntity, context) }
    }
}