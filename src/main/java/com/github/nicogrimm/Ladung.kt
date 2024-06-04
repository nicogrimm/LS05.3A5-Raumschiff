package com.github.nicogrimm

class Ladung(var name: String = "Leerladung", var einheiten: Int = 0) {
    var besitzer: LadungsBesitzer? = null

    override fun toString(): String {
        return "Ladung(name='$name', einheiten=$einheiten)"
    }
}