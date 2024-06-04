package com.github.nicogrimm

class Planet(val name: String, val atmosphaere: Boolean, val posX: Int, val posY: Int): LadungsBesitzer {
    val koordinaten: Pair<Int, Int>
        get() = this.posX to this.posY

    override val ladungen: MutableList<Ladung> = mutableListOf()

    override fun addLadung(ladung: Ladung) {
        ladungen.add(ladung)
    }

    override fun removeLadung(ladung: Ladung) {
        ladungen.remove(ladung)
    }
}
