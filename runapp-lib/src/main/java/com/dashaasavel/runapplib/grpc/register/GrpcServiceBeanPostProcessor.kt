package com.dashaasavel.runapplib.grpc.register

import com.dashaasavel.runapplib.logger
import io.grpc.BindableService
import io.grpc.util.MutableHandlerRegistry
import org.springframework.beans.factory.config.BeanPostProcessor
import java.lang.Exception

class GrpcServiceBeanPostProcessor(
    private val handlerRegistry: MutableHandlerRegistry
): BeanPostProcessor {
    private val logger = logger()

    private val bindableServiceBeanNames = mutableSetOf<String>()
    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        val javaClass = bean.javaClass
        javaClass.getAnnotation(GrpcService::class.java)?: return bean
        bindableServiceBeanNames+= beanName
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bindableServiceBeanNames.contains(beanName)) {
            try {
                handlerRegistry.addService(bean as BindableService)
                logger.info("{} successfully registered as grpc-service", bean.javaClass.simpleName)
            } catch (e: Exception) {
                logger.warn("{} bean is not a grpc service", beanName)
            }
        }
        return bean
    }
}