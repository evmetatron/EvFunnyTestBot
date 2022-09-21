/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.fixtures

import com.evmetatron.evfunnytest.enumerable.AllowGender
import com.evmetatron.evfunnytest.enumerable.Gender
import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.storage.db.entity.QuestionReplaceEntity
import com.evmetatron.evfunnytest.storage.db.entity.ResultReplaceEntity
import com.evmetatron.evfunnytest.storage.db.entity.TestEntity
import com.evmetatron.evfunnytest.storage.db.entity.TestReplaceViewEntity
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentAnswerEntity
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity
import com.evmetatron.evfunnytest.storage.memory.entity.RemoveButtonsEntity

fun testEntity5(): TestEntity =
    TestEntity(
        id = 5,
        name = "Test 5",
        description = "Description test 5",
        type = TestType.REPLACE,
        allowGender = AllowGender.ALL,
    )

fun testEntity6(): TestEntity =
    TestEntity(
        id = 6,
        name = "Test 6",
        description = "Description test 6",
        type = TestType.REPLACE,
        allowGender = AllowGender.ALL,
    )

fun testEntity7(): TestEntity =
    TestEntity(
        id = 7,
        name = "Test 7",
        description = "Description test 7",
        type = TestType.REPLACE,
        allowGender = AllowGender.ALL,
    )

fun testReplaceViewEntity1() =
    TestReplaceViewEntity(
        id = 1,
        name = "Test 1",
        description = "Description test 1",
        allowGender = AllowGender.FOR_ONE,
        questions = listOf(
            QuestionReplaceEntity(
                num = 1,
                question = "Question 1",
            ),
            QuestionReplaceEntity(
                num = 2,
                question = "Question 2",
            ),
            QuestionReplaceEntity(
                num = 3,
                question = "Question 3",
            ),
            QuestionReplaceEntity(
                num = 4,
                question = "Question 4",
            ),
        ),
        results = listOf(
            ResultReplaceEntity(
                gender = Gender.MALE,
                result = "Result for male",
            ),
            ResultReplaceEntity(
                gender = Gender.FEMALE,
                result = "Result for female",
            ),
        ),
    )

fun createCurrentTestEntity(
    userId: Long = faker.number().randomNumber(),
    testId: Long = faker.number().randomNumber(),
    type: TestType = rndEnum(),
    gender: Gender? = rndEnum<Gender>(),
    allowGender: AllowGender = rndEnum(),
    answers: List<CurrentAnswerEntity> = (1..3).map { createCurrentAnswerEntity() },
): CurrentTestEntity =
    CurrentTestEntity(
        userId = userId,
        testId = testId,
        type = type,
        gender = gender,
        allowGender = allowGender,
        answers = answers,
    )

fun createTestEntity(
    id: Long = faker.number().randomNumber(),
    name: String = faker.harryPotter().quote(),
    description: String = faker.harryPotter().quote(),
    type: TestType = rndEnum(),
    allowGender: AllowGender = rndEnum(),
): TestEntity =
    TestEntity(
        id = id,
        name = name,
        description = description,
        type = type,
        allowGender = allowGender,
    )

fun createCurrentAnswerEntity(
    num: Int = faker.number().randomDigit(),
    answer: String = faker.harryPotter().quote(),
): CurrentAnswerEntity =
    CurrentAnswerEntity(
        num = num,
        answer = answer,
    )

fun createRemoveButtonsEntity(
    userId: Long = faker.number().randomNumber(),
    chatId: Long = faker.number().randomNumber(),
    messageIds: List<Int> = (1..3).map { faker.number().randomDigit() },
): RemoveButtonsEntity =
    RemoveButtonsEntity(
        userId = userId,
        chatId = chatId,
        messageIds = messageIds,
    )

fun createTestReplaceViewEntity(
    id: Long = faker.number().randomNumber(),
    name: String = faker.harryPotter().quote(),
    description: String = faker.harryPotter().quote(),
    allowGender: AllowGender = rndEnum(),
    questions: List<QuestionReplaceEntity> = (1..5).map { createQuestionReplaceEntity() },
    results: List<ResultReplaceEntity> = Gender.values().map { createResultReplaceEntity(gender = it) },
): TestReplaceViewEntity =
    TestReplaceViewEntity(
        id = id,
        name = name,
        description = description,
        allowGender = allowGender,
        questions = questions,
        results = results,
    )

fun createQuestionReplaceEntity(
    num: Int = faker.number().randomDigit(),
    question: String = faker.harryPotter().quote(),
): QuestionReplaceEntity =
    QuestionReplaceEntity(
        num = num,
        question = question,
    )

fun createResultReplaceEntity(
    gender: Gender? = rndEnum<Gender>(),
    result: String = faker.harryPotter().quote(),
): ResultReplaceEntity =
    ResultReplaceEntity(
        gender = gender,
        result = result,
    )
