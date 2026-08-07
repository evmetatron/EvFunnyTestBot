package io.github.evmetatron.evfunnytest.enumerable

import io.github.evmetatron.evfunnytest.dto.button.ConcreteButton
import io.github.evmetatron.evfunnytest.dto.button.GenderButton
import io.github.evmetatron.evfunnytest.dto.button.GetTestButton
import io.github.evmetatron.evfunnytest.dto.button.PageButton
import io.github.evmetatron.evfunnytest.dto.button.StartTestButton
import io.github.evmetatron.evfunnytest.dto.button.TestVariableButton

enum class ButtonType {
    PAGE {
        override fun getConcreteButton(map: Map<String, String>): ConcreteButton =
            PageButton.ofMap(map)
    },
    GET_TEST {
        override fun getConcreteButton(map: Map<String, String>): ConcreteButton =
            GetTestButton.ofMap(map)
    },
    START_TEST {
        override fun getConcreteButton(map: Map<String, String>): ConcreteButton =
            StartTestButton.ofMap(map)
    },
    CANCEL_ANSWER {
        override fun getConcreteButton(map: Map<String, String>): ConcreteButton? = null
    },
    EXIT_TEST {
        override fun getConcreteButton(map: Map<String, String>): ConcreteButton? = null
    },
    SELECT_GENDER {
        override fun getConcreteButton(map: Map<String, String>): ConcreteButton =
            GenderButton.ofMap(map)
    },
    TEST_VARIABLE {
        override fun getConcreteButton(map: Map<String, String>): ConcreteButton =
            TestVariableButton.ofMap(map)
    };

    abstract fun getConcreteButton(map: Map<String, String>): ConcreteButton?
}
