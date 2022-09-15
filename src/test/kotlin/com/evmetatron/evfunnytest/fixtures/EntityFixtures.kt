/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.fixtures

import com.evmetatron.evfunnytest.enumerable.TestType
import com.evmetatron.evfunnytest.storage.db.entity.TestEntity
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentAnswerEntity
import com.evmetatron.evfunnytest.storage.memory.entity.CurrentTestEntity

fun testEntity5(): TestEntity =
    TestEntity(
        id = 5,
        name = "Test 5",
        description = "Description test 5",
        type = TestType.REPLACE,
    )

fun testEntity6(): TestEntity =
    TestEntity(
        id = 6,
        name = "Test 6",
        description = "Description test 6",
        type = TestType.REPLACE,
    )

fun testEntity7(): TestEntity =
    TestEntity(
        id = 7,
        name = "Test 7",
        description = "Description test 7",
        type = TestType.REPLACE,
    )

fun createCurrentTestEntity(
    userId: Long = faker.number().randomNumber(),
    testId: Long = faker.number().randomNumber(),
    type: TestType = rndEnum(),
    answers: List<CurrentAnswerEntity> = (1..3).map { createCurrentAnswerEntity() },
): CurrentTestEntity =
    CurrentTestEntity(
        userId = userId,
        testId = testId,
        type = type,
        answers = answers,
    )

fun createTestEntity(
    id: Long = faker.number().randomNumber(),
    name: String = faker.harryPotter().quote(),
    description: String = faker.harryPotter().quote(),
    type: TestType = rndEnum(),
): TestEntity =
    TestEntity(
        id = id,
        name = name,
        description = description,
        type = type,
    )

fun createCurrentAnswerEntity(
    num: Int = faker.number().randomDigit(),
    answer: String = faker.harryPotter().quote(),
): CurrentAnswerEntity =
    CurrentAnswerEntity(
        num = num,
        answer = answer,
    )
