package com.dashaasavel.runapplib.grpc.register

import io.grpc.BindableService
import io.grpc.util.MutableHandlerRegistry
import org.springframework.beans.factory.config.BeanPostProcessor
import java.lang.Exception

class GrpcServiceBeanPostProcessor(
    private val handlerRegistry: MutableHandlerRegistry
): BeanPostProcessor {
    private val bindableServiceBeanNames = mutableSetOf<String>()
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        val javaClass = bean.javaClass
        javaClass.getAnnotation(GrpcService::class.java)?: return bean
        bindableServiceBeanNames+= beanName
        println("${javaClass.simpleName} is GrpcService!!!")
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bindableServiceBeanNames.contains(beanName)) {
            try {
                handlerRegistry.addService(bean as BindableService)
            } catch (e: Exception) {
                println("Нацепили на гавно! че такое по вашему $beanName ???")
            }
        }
        return bean
    }
}