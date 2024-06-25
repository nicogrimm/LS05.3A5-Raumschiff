package com.github.nicogrimm

enum class Befehl {
    Bewegen, Stehenbleiben, LadungenAnzeigen, KoordinatenAnzeigen, RaumschiffStatusAnzeigen, Hilfe, SpielBeenden;

    companion object {
        fun fromChar(c: Char): Befehl? {
            return when (c) {
                'q' -> SpielBeenden
                'w', 'd', 's', 'a' -> Bewegen
                'b' -> Stehenbleiben
                'l' -> LadungenAnzeigen
                'k' -> KoordinatenAnzeigen
                'i' -> RaumschiffStatusAnzeigen
                '?' -> Hilfe
                else -> null
            }
        }
    }
}