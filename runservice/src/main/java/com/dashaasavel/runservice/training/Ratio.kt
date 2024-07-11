package com.dashaasavel.runservice.training

import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Суть класса -- рассчитать по неделям общую дистанцию для пробежек для подготовки к марафону
 * Последняя неделя учитывает марафон, нагрузка растет и потом снижается постепенно
 */
object Ratio {
    fun ratio(weeks: Int, longRun: Int): IntArray {
        val hardWeeks = weeks - 4
        val ratio: Double
        var lastDistance = 60.0
        var distance: Double = if (longRun == 0) {
            5.0
        } else (longRun * 2).toDouble()

        if (hardWeeks > 17) {
            lastDistance = 70.0
        } else if (hardWeeks > 13) {
            lastDistance = 64.0
        }
        val pow = (hardWeeks - 1).toDouble()
        val res = lastDistance / distance
        ratio = res.pow(1 / pow)
        val run = IntArray(weeks)
        run[0] = distance.toInt()
        for (i in 1 until hardWeeks) {
            distance *= ratio
            run[i] = distance.roundToInt()
        }
        val ratioToLow = 0.68
        for (i in hardWeeks until weeks - 1) {
            run[i] = ceil(run[i - 1] * ratioToLow).toInt()
        }
        run[weeks - 1] = 52
        return run
    }
}