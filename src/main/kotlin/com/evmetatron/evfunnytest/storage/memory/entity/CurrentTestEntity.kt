/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.storage.memory.entity

import com.evmetatron.evfunnytest.enumerable.AllowGender
import com.evmetatron.evfunnytest.enumerable.Gender
import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.exception.AnswerException
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("current_test", timeToLive = 7200)
data class CurrentTestEntity(
    @Id
    val userId: Long,
    val testId: Long,
    val type: TestType,
    val gender: Gender? = null,
    val allowGender: AllowGender,
    val answers: List<CurrentAnswerEntity> = emptyList(),
) {
    fun withGender(gender: Gender): CurrentTestEntity =
        this.copy(gender = gender)

    fun withAnswer(answer: String): CurrentTestEntity =
        this.copy(
            answers = this.answers + CurrentAnswerEntity(
                (this.answers.lastOrNull()?.num ?: 0) + 1,
                answer,
            )
        )

    fun withoutAnswer(): CurrentTestEntity =
        if (this.answers.isNotEmpty()) {
            this.copy(answers = this.answers.dropLast(1))
        } else if (this.gender != null) {
            this.copy(gender = null)
        } else {
            throw AnswerException("Ошибка при отмене ответа")
        }

    fun getNeedAnswerNum(): Int =
        (this.answers.lastOrNull()?.num ?: 0) + 1

    fun isNeedGender(): Boolean =
        this.allowGender == AllowGender.FOR_ONE && this.gender == null
}
