package com.github.nicogrimm

enum class Befehl {
    Bewegen, Stehenbleiben, LadungenAuflisten, KoordinatenAnzeigen, Hilfe, SpielBeenden;

    companion object {
        fun fromChar(c: Char): Befehl? {
            return when (c) {
                'q' -> SpielBeenden
                'w', 'd', 's', 'a' -> Bewegen
                'b' -> Stehenbleiben
                'l' -> LadungenAuflisten
                'k' -> KoordinatenAnzeigen
                '?' -> Hilfe
                else -> null
            }
        }
    }
}