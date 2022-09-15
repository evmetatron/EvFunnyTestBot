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

    fun withAnswer(answer: String, num: Int): CurrentTestEntity =
        (this.answers.lastOrNull()?.num ?: 0)
            .takeIf { it + 1 == num }
            ?.let {
                this.copy(answers = this.answers + CurrentAnswerEntity(num, answer))
            } ?: throw AnswerException("Нарушен порядок при добавлении ответа")

    fun withoutAnswer(num: Int): CurrentTestEntity =
        this.answers.lastOrNull()
            ?.num
            ?.takeIf { it == num }
            ?.let {
                this.copy(answers = this.answers.dropLast(1))
            } ?: throw AnswerException("Нарушен порядок при удалении ответа")

    fun isNeedGender(): Boolean =
        this.allowGender == AllowGender.FOR_ONE && this.gender == null
}
