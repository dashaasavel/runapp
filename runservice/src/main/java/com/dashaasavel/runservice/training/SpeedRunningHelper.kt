package com.dashaasavel.runservice.training

import java.util.*
/**
 * idk как составлять правильно тренировки на скорость, поэтому используем random :)
 */
object SpeedRunningHelper {
    private val level1 = listOf(Pair(6, 100), Pair(4, 150), Pair(3, 200))
    private val level2 = listOf(Pair(5, 300), Pair(3, 500), Pair(4, 400))
    private val level3 = listOf(Pair(5, 600), Pair(6, 500), Pair(4, 750))

    fun getSpeedTraining(level: Int): Pair<Int, Int> {
        val random = Random().nextInt()
        return when(level) {
            1 -> level1[random]
            2 -> level2[random]
            3 -> level3[random]
            else -> error("unreachable code")
        }
    }
}