package com.dashaasavel.runapplib.grpc.register

/**
 * Аннотация, с помощью которой можно зарегистрировать grpc-сервис
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class GrpcService()
