package com.github.nicogrimm

enum class Richtung {
    Oben, Rechts, Unten, Links;

    companion object {
        fun fromChar(c: Char): Richtung? {
            return when (c) {
                'w' -> Oben
                'd' -> Rechts
                's' -> Unten
                'a' -> Links
                else -> null
            }
        }
    }
}
