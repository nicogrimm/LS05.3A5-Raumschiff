package com.github.nicogrimm

interface LadungsBesitzer {
    val ladungen: List<Ladung>

    fun addLadung(ladung: Ladung)
    fun removeLadung(ladung: Ladung)
}