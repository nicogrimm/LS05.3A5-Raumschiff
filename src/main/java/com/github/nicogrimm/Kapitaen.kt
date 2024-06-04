package com.github.nicogrimm

import kotlin.math.floor
import kotlin.random.Random

class Kapitaen(var name: String, var charisma: Int, var erfahrung: Int) {
    var raumschiff: Raumschiff? = null

    fun diplomatischeVerhandlung() =
        floor(erfahrung * 2 + charisma * (Random.nextInt(5, 15).toDouble() / 10)).toInt()
}