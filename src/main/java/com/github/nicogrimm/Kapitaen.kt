package com.github.nicogrimm

import kotlin.math.floor
import kotlin.random.Random

class Kapitaen(var name: String, charisma: Int, erfahrung: Int) {
    var raumschiff: Raumschiff? = null

    var charisma: Int = 0
        set(value) {
            if (value !in 1..10) {
                throw IllegalArgumentException("Charisma muss im Bereich von 1 bis 10 sein")
            }
            field = value
        }

    var erfahrung: Int = 0
        set(value) {
            if (value !in 1..10) {
                throw IllegalArgumentException("Erfahrung muss im Bereich von 1 bis 10 sein")
            }
            field = value
        }

    init {
        this.charisma = charisma
        this.erfahrung = erfahrung
    }

    fun diplomatischeVerhandlungsWert() =
        floor(erfahrung * 2 + charisma * (Random.nextInt(5, 15).toDouble() / 10)).toInt()
}