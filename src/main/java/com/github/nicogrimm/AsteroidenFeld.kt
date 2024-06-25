package com.github.nicogrimm

import kotlin.random.Random

class AsteroidenFeld(val posX: Int, val posY: Int, val gefahrdenPotenzial: Int) {
    val koordinaten: Pair<Int, Int>
        get() = this.posX to this.posY

    fun raumschiffKollison(raumschiff: Raumschiff): Int {
        val gefahr = this.gefahrdenPotenzial.toDouble() / 100
        val ausweichChance = raumschiff.asteroidenAusweichChance() - (gefahr - 0.3)

        if (Random.nextDouble() < ausweichChance) {
            return (this.gefahrdenPotenzial.toDouble() * Random.Default.nextDouble(0.9, 1.2)).toInt()
        }
        return 0
    }
}