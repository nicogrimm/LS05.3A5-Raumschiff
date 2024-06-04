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
        koordinatenAnzeigen(eos)

        val richtung = befehlEingabe(scanner, eos) ?: break

        eos.fliegen(richtung)

        if (eos.koordinaten == aurora.koordinaten) {
            println("Hier ist das Raumschiff ${aurora.name}")
        }

        for (planet in listOf(auroria, solaria, ktaris)) {
            if (eos.koordinaten == planet.koordinaten) {
                println("Hier ist der Planet ${planet.name}. Dieser Planet hat ${if (planet.atmosphaere) "eine" else "keine"} Atmosphaere.")
                print("Möchtest du mit diesem Planeten Ladungen tauschen? (y/n) ")
                val char = try {
                    scanner.nextLine()[0]
                } catch (e: StringIndexOutOfBoundsException) {
                    ' '
                }

                if (char == 'y') {
                    tauschen(scanner, eos, planet)
                }
            }
        }
    }
}

private fun koordinatenAnzeigen(eos: Raumschiff) {
    println("Raumschiff Koordinaten: ${eos.koordinaten}")
}

fun befehlEingabe(scanner: Scanner, eos: Raumschiff): Richtung? {
    while (true) {
        print("Befehl (w/a/s/d/.../?): ")
        val char = try {
            scanner.nextLine()[0]
        } catch (e: StringIndexOutOfBoundsException) {
            ' '
        }

        when (char) {
            'q' -> return null
            'l' -> {
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

            'k' -> {
                koordinatenAnzeigen(eos)
                continue
            }

            '?' -> {
                println(
                    """
                    Spiel beenden mit 'q'
                    Bewegung mit 'w' (hoch), 'a' (links), 's' (unten) und 'd' (rechts)
                    Ladungen auflisten mit 'l'
                    Koordinaten anzeigen mit 'k'
                    Diese Hilfe anzeigen mit '?'
                """.trimIndent()
                )
                continue
            }
        }

        val richtung = Richtung.fromChar(char)

        if (richtung == null) {
            println("FEHLER: Unbekannter Befehl: '$char'")
            continue
        }

        return richtung
    }
}

fun tauschen(scanner: Scanner, raumschiff: Raumschiff, planet: Planet) {
    var wiederholen = true;
    while (wiederholen) {
        println()

        val auswahl: Int;
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
                val ladung = ladungAuswaehlen("Welche Ladung möchstest du geben?", raumschiff.ladungen)
                if (ladung != null) {
                    raumschiff.removeLadung(ladung)
                    planet.addLadung(ladung)
                } else {
                    wiederholen = false;
                }
            }

            2 -> {
                val ladung = ladungAuswaehlen("Welche Ladung möchstest du erhalten?", planet.ladungen)
                if (ladung != null) {
                    planet.removeLadung(ladung)
                    raumschiff.addLadung(ladung)
                } else {
                    wiederholen = false;
                }
            }

            else -> throw NotImplementedError("Dieser Code sollte nicht erreichbar sein")
        }

        if (wiederholen) {
            print("Möchtest du noch mehr tauschen? (y/n) ")
            val wiederholungChar = try {
                scanner.nextLine()[0]
            } catch (e: StringIndexOutOfBoundsException) {
                ' '
            }

            wiederholen = wiederholungChar == 'y'
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