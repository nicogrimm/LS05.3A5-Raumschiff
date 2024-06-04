package com.github.nicogrimm

class Planet(val name: String, val atmosphaere: Boolean, val posX: Int, val posY: Int) {
    val koordinaten: Pair<Int, Int>
        get() = this.posX to this.posY
}
