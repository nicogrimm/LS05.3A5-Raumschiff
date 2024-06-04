package com.github.nicogrimm

class Raumschiff(val name: String, var posX: Int = 0, var posY: Int = 0) {
    fun fliegen(richtung: Richtung) {
        when (richtung) {
            Richtung.Oben -> this.posY -= 1
            Richtung.Links -> this.posX -= 1
            Richtung.Unten -> this.posY += 1
            Richtung.Rechts -> this.posX += 1
        }
    }

    val koordinaten: List<Int>
        get() = listOf(this.posX, this.posY)

    fun koordinatenAlsString() = "($posX, $posY)"
}