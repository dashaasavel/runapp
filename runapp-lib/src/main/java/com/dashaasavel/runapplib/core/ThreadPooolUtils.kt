package com.dashaasavel.runapplib.core

import java.util.concurrent.ExecutorService
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Кэшируем потоки, так же как и [java.util.concurrent.Executors.newCachedThreadPool],
 * но максимальное кол-во потоков не Integer.MAX_VALUE, а заданное в аргументах метода
 */
fun createCachedThreadPoolWithLimit(maximumPoolSize: Int): ExecutorService {
    return ThreadPoolExecutor(
        0, maximumPoolSize,
        60L, TimeUnit.SECONDS,
        SynchronousQueue()
    )
}