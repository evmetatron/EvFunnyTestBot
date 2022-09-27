/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.enumerable

import com.evmetatron.evfunnytest.dto.button.ConcreteButton
import com.evmetatron.evfunnytest.dto.button.GenderButton
import com.evmetatron.evfunnytest.dto.button.GetTestButton
import com.evmetatron.evfunnytest.dto.button.PageButton
import com.evmetatron.evfunnytest.dto.button.StartTestButton
import com.evmetatron.evfunnytest.dto.button.TestVariableButton

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
