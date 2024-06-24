package com.github.nicogrimm

import com.github.nicogrimm.util.ConsoleHelper
import java.util.*

fun main() {
    val alexia = Kapitaen("Alexia Nova", 8, 6)
    val zenith = Kapitaen("Zenith Nightfall", 7, 8)

    alexia.name = "Alexia Starlight Nova"

    val eos = Raumschiff("Eos Nova", 0, 2, kapitaen = alexia)
    val aurora = Raumschiff("Aurora Quest", 1, 3, kapitaen = zenith)

    val auroria = Planet("Auroria", true, 0, 2)
    val solaria = Planet("Solara", true, 4, 6)
    val ktaris = Planet("Ktaris", false, 10, 3)

    val gemuese = Ladung("Gemuese", 10)
    val nanobots = Ladung("Nanobots", 15)
    val antimaterieWaffen = Ladung("Antimaterie-Waffen", 125)
    val medizin = Ladung("Medizin", 13)

    ktaris.addLadung(gemuese)
    ktaris.addLadung(nanobots)
    solaria.addLadung(antimaterieWaffen)
    auroria.addLadung(medizin)

    println("Sie fliegen das Raumschiff ${eos.name} gesteuert von ${eos.kapitaen?.name ?: "niemanden"}")

    val scanner = Scanner(System.`in`)
    val gameOver = false
    while (!gameOver) {
        val umgebung = umgebungAlsTextZeilen(eos, 5, 5, listOf(eos, aurora), listOf(auroria, solaria, ktaris));

        for ((i, zeile) in umgebung.withIndex()) {
            if (i == 1) {
                print("$zeile ")
                koordinatenAnzeigen(eos)
            } else {
                println(zeile)
            }
        }

        val eingabe = befehlEingabe(scanner)

        when (eingabe.second) {
            Befehl.SpielBeenden -> break
            Befehl.LadungenAuflisten -> {
                if (eos.ladungen.isEmpty()) {
                    println("Du hast keine Ladungen")
                    continue
                }

                println("Deine Ladungen: ")
                for (ladung in eos.ladungen) {
                    println("    $ladung")
                }
                continue
            }

            Befehl.KoordinatenAnzeigen -> {
                koordinatenAnzeigen(eos)
                continue
            }

            Befehl.Hilfe -> {
                println(
                    """
                    Spiel beenden mit 'q'
                    Bewegung mit 'w' (hoch), 'a' (links), 's' (unten) und 'd' (rechts)
                    Ladungen auflisten mit 'l'
                    Koordinaten anzeigen mit 'k'
                    Stehen bleiben mit 'b'
                    Diese Hilfe anzeigen mit '?'

                    Symbole:
                    x - (Dein) Raumschiff Eos
                    > - Raumschiff
                    O - Planet
                """.trimIndent()
                )
                continue
            }

            Befehl.Bewegen -> {
                val richtung = Richtung.fromChar(eingabe.first)
                    ?: throw RuntimeException("Dieser Fehler sollte nie passieren (Unbekannte Richtung, die als Richtungsbefehl erkannt wurde)")
                eos.fliegen(richtung)
            }

            Befehl.Stehenbleiben -> {}
        }


        for (raumschiff in listOf(aurora)) {
            if (eos.koordinaten == raumschiff.koordinaten) {
                println("Hier ist das Raumschiff ${raumschiff.name} gesteuert von ${raumschiff.kapitaen?.name ?: "niemanden"}")
                print("Möchtest du dieses Raumschiff angreifen? (y/n) ")
                if (charLesen(scanner) == 'y') {
                    eos.angreifen(raumschiff)

                    if (raumschiff.integritaet == 0) {
                        println("${raumschiff.name} wurde zerstört")
                    }
                }
            }
        }

        for (planet in listOf(auroria, solaria, ktaris)) {
            if (eos.koordinaten == planet.koordinaten) {
                println("Hier ist der Planet ${planet.name}. Dieser Planet hat ${if (planet.atmosphaere) "eine" else "keine"} Atmosphaere.")
                print("Möchtest du mit diesem Planeten Ladungen tauschen? (y/n) ")

                if (charLesen(scanner) == 'y') {
                    tauschen(scanner, eos, planet)
                }
            }
        }
    }
}

fun charLesen(scanner: Scanner): Char {
    return try {
        scanner.nextLine()[0]
    } catch (e: StringIndexOutOfBoundsException) {
        ' '
    }
}

fun koordinatenAnzeigen(eos: Raumschiff) {
    println("Raumschiff Koordinaten: ${eos.koordinaten}")
}

fun umgebungAlsTextZeilen(
    eos: Raumschiff,
    sichtweiteX: Int,
    sichtweiteY: Int,
    raumschiffe: List<Raumschiff>,
    planeten: List<Planet>
): List<String> {
    val zeilen = mutableListOf<String>()
    zeilen.add("+" + "-".repeat(sichtweiteX * 2 + 1) + "+")

    for (dy in -sichtweiteX..sichtweiteX) {
        val y = eos.posY + dy;
        var zeile = "|"

        koordinatenLoop@ for (dx in -sichtweiteY..sichtweiteY) {
            val x = eos.posX + dx;
            val koordinaten = x to y

            if (eos.koordinaten == koordinaten) {
                zeile += "x"
                continue
            }
            for (raumschiff in raumschiffe) {
                if (raumschiff.koordinaten == koordinaten) {
                    zeile += ">"
                    continue@koordinatenLoop
                }
            }
            for (planet in planeten) {
                if (planet.koordinaten == koordinaten) {
                    zeile += "O"
                    continue@koordinatenLoop
                }
            }

            zeile += " "
        }
        zeile += "|"
        zeilen.add(zeile)
    }

    zeilen.add("+" + "-".repeat(sichtweiteX * 2 + 1) + "+")
    return zeilen
}

fun befehlEingabe(scanner: Scanner): Pair<Char, Befehl> {
    while (true) {
        print("Befehl (w/a/s/d/.../?): ")
        val char = charLesen(scanner)
        val befehl = Befehl.fromChar(char)

        if (befehl != null) {
            return char to befehl
        }

        println("FEHLER: Unbekannter Befehl: '$char'")
    }
}

fun tauschen(scanner: Scanner, raumschiff: Raumschiff, planet: Planet) {
    var wiederholen = true
    while (wiederholen) {
        println()

        val auswahl: Int
        if (raumschiff.ladungen.isEmpty() && planet.ladungen.isEmpty()) {
            println("Weder du noch der Planet hat Ladungen")
            return
        } else if (raumschiff.ladungen.isEmpty()) {
            println("Du hast keine Ladungen und du kannst nur welche erhalten.")
            auswahl = 2
        } else if (planet.ladungen.isEmpty()) {
            println("Der Planet hat keine Ladungen und du kannst nur welche geben.")
            auswahl = 1
        } else {
            auswahl = ConsoleHelper.printMenu("Möchtest du Ladungen geben oder erhalten?", "Geben", "Erhalten")
        }

        when (auswahl) {
            1 -> {
                val ladung = ladungAuswaehlen("Welche Ladung möchtest du geben?", raumschiff.ladungen)
                if (ladung != null) {
                    raumschiff.removeLadung(ladung)
                    planet.addLadung(ladung)
                } else {
                    wiederholen = false
                }
            }

            2 -> {
                val ladung = ladungAuswaehlen("Welche Ladung möchtest du erhalten?", planet.ladungen)
                if (ladung != null) {
                    planet.removeLadung(ladung)
                    raumschiff.addLadung(ladung)
                } else {
                    wiederholen = false
                }
            }

            else -> throw NotImplementedError("Dieser Code sollte nicht erreichbar sein")
        }

        if (wiederholen) {
            print("Möchtest du noch mehr tauschen? (y/n) ")
            wiederholen = charLesen(scanner) == 'y'
        }
    }

    println()
    println("Danke und bis auf Wiedersehen!")
}

fun ladungAuswaehlen(header: String, ladungen: List<Ladung>): Ladung? {
    val auswahl = ConsoleHelper.printMenuOpt(header, ArrayList(ladungen))
    if (auswahl == 0) {
        return null
    }
    return ladungen[auswahl - 1]
}