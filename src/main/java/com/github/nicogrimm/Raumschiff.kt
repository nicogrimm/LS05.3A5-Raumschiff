package com.github.nicogrimm

class Raumschiff(val name: String, var posX: Int = 0, var posY: Int = 0, kapitaen: Kapitaen? = null): LadungsBesitzer {
    var kapitaen: Kapitaen? = null
        set(value) {
            kapitaen?.raumschiff = null

            field = value
            value?.raumschiff = this
        }

    init {
        this.kapitaen = kapitaen
    }

    fun fliegen(richtung: Richtung) {
        when (richtung) {
            Richtung.Oben -> this.posY -= 1
            Richtung.Links -> this.posX -= 1
            Richtung.Unten -> this.posY += 1
            Richtung.Rechts -> this.posX += 1
        }
    }

    val koordinaten: Pair<Int, Int>
        get() = this.posX to this.posY

    override val ladungen: MutableList<Ladung> = mutableListOf()

    override fun addLadung(ladung: Ladung) {
        ladung.besitzer?.removeLadung(ladung)

        ladung.besitzer = this
        ladungen.add(ladung)
    }

    override fun removeLadung(ladung: Ladung) {
        ladung.besitzer = null
        ladungen.remove(ladung)
    }
}