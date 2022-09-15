/*
 * Код написан разработчиком evmetatron (artem.nagibin.89@ya.ru)
 * Репозиторий приложения: https://github.com/evmetatron/EvFunnyTestBot
 */

package com.evmetatron.evfunnytest.infrastructure

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

internal class ChainOfResponsibilityFactory : ApplicationContextAware {
    private lateinit var applicationContext: ApplicationContext
    inline fun <reified T : Any> createChain(vararg chains: KClass<*>): T? {
        var prev: T? = null

        val chainsDescending = chains.mapIndexed { index, kClass -> index to kClass }
            .sortedByDescending { (index, _) -> index }
            .map { (_, kClass) -> kClass }

        chainsDescending.forEach { clazz ->
            clazz.primaryConstructor?.parameters
                ?.map { parameter ->
                    if (parameter.type.jvmErasure.java == T::class.java) {
                        prev
                    } else if (applicationContext::class.isSubclassOf(parameter.type.jvmErasure)) {
                        applicationContext
                    } else {
                        applicationContext.autowireCapableBeanFactory.getBean(parameter.type.jvmErasure.java)
                    }
                }
                ?.apply {
                    @Suppress("SpreadOperator")
                    prev = clazz.primaryConstructor?.call(*this.toTypedArray()) as T?
                }
        }

        return prev
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }
}
