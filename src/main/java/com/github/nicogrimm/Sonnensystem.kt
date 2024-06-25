package com.github.nicogrimm

import com.github.nicogrimm.util.ConsoleHelper
import java.util.*

class SpielStand(
    val eos: Raumschiff,
    val raumschiffe: MutableList<Raumschiff>,
    val planeten: MutableList<Planet>,
    val asteroidenFelder: MutableList<AsteroidenFeld>
)

fun main() {
    val spielStand = init()

    println("Sie fliegen das Raumschiff ${spielStand.eos.name} gesteuert von ${spielStand.eos.kapitaen?.name ?: "niemanden"}")

    val scanner = Scanner(System.`in`)
    var gameOver = false
    while (!gameOver) {
        val umgebung = umgebungAlsTextZeilen(spielStand, 5, 5)

        for ((i, zeile) in umgebung.withIndex()) {
            if (i == 1) {
                print("$zeile ")
                koordinatenAnzeigen(spielStand.eos)
            } else {
                println(zeile)
            }
        }

        val eingabe = befehlEingabe(scanner)

        when (eingabe.second) {
            Befehl.SpielBeenden -> break
            Befehl.LadungenAnzeigen -> {
                if (spielStand.eos.ladungen.isEmpty()) {
                    println("Du hast keine Ladungen")
                    continue
                }

                println("Deine Ladungen: ")
                for (ladung in spielStand.eos.ladungen) {
                    println("    $ladung")
                }
                continue
            }

            Befehl.KoordinatenAnzeigen -> {
                koordinatenAnzeigen(spielStand.eos)
                continue
            }

            Befehl.RaumschiffStatusAnzeigen -> {
                println("Status von ${spielStand.eos.name}:\n" +
                        "    Integrität: ${spielStand.eos.integritaet}\n" +
                        "    Energieschild: ${spielStand.eos.energieSchild}")
                continue
            }

            Befehl.Hilfe -> {
                println(
                    """
                    Befehle:
                    Spiel beenden mit 'q'
                    Bewegung mit 'w' (hoch), 'a' (links), 's' (unten) und 'd' (rechts)
                    Ladungen anzeigen mit 'l'
                    Koordinaten anzeigen mit 'k'
                    Status deines Raumschiffes anzeigen mit 'i'
                    Stehen bleiben mit 'b'
                    Diese Hilfe anzeigen mit '?'

                    Symbole:
                    x - (Dein) Raumschiff Eos
                    > - Raumschiff
                    O - Planet
                    # - Asteroiden Feld
                """.trimIndent()
                )
                continue
            }

            Befehl.Bewegen -> {
                val richtung = Richtung.fromChar(eingabe.first)
                    ?: throw RuntimeException("Dieser Fehler sollte nie passieren (Unbekannte Richtung, die als Richtungsbefehl erkannt wurde)")
                spielStand.eos.fliegen(richtung)
            }

            Befehl.Stehenbleiben -> {}
        }


        val it = spielStand.raumschiffe.listIterator()
        while (it.hasNext()) {
            val raumschiff = it.next()
            if (raumschiff != spielStand.eos && spielStand.eos.koordinaten == raumschiff.koordinaten) {
                println("Hier ist das Raumschiff ${raumschiff.name} gesteuert von ${raumschiff.kapitaen?.name ?: "niemanden"}")
                print("Möchtest du dieses Raumschiff angreifen? (y/n) ")
                if (charLesen(scanner) == 'y') {
                    spielStand.eos.angreifen(raumschiff)

                    if (raumschiff.integritaet == 0) {
                        println("${raumschiff.name} wurde zerstört")
                        it.remove()
                    }
                }
            }
        }

        for (planet in spielStand.planeten) {
            if (spielStand.eos.koordinaten == planet.koordinaten) {
                println("Hier ist der Planet ${planet.name}. Dieser Planet hat ${if (planet.atmosphaere) "eine" else "keine"} Atmosphaere.")
                print("Möchtest du mit diesem Planeten Ladungen tauschen? (y/n) ")

                if (charLesen(scanner) == 'y') {
                    tauschen(scanner, spielStand.eos, planet)
                }
            }
        }

        for (feld in spielStand.asteroidenFelder) {
            if (spielStand.eos.koordinaten == feld.koordinaten) {
                println("Hier ist ein Asteroidenfeld.")

                val schaden = feld.raumschiffKollison(spielStand.eos)
                if (schaden > 0) {
                    println("Du konntest das Asteroidenfeld nicht so gut navigieren")
                    spielStand.eos.schadenNehmen(schaden)
                } else {
                    println("Du konntest das Asteroidenfeld problemlos navigieren.")
                }
            }
        }

        if (spielStand.eos.integritaet == 0) {
            gameOver = true

            println("Dein Raumschiff ${spielStand.eos.name} wurde zerstört.")
            println("===== GAME OVER =====")
        }
    }
}

fun init(): SpielStand {
    val alexia = Kapitaen("Alexia Nova", 8, 6)
    val zenith = Kapitaen("Zenith Nightfall", 7, 8)

    alexia.name = "Alexia Starlight Nova"

    val raumschiffe = mutableListOf<Raumschiff>()
    val planeten = mutableListOf<Planet>()

    val eos = Raumschiff("Eos Nova", 0, 2, kapitaen = alexia)
    raumschiffe.add(eos)
    val aurora = Raumschiff("Aurora Quest", 1, 3, kapitaen = zenith)
    raumschiffe.add(aurora)

    val auroria = Planet("Auroria", true, 0, 2)
    planeten.add(auroria)
    val solaria = Planet("Solara", true, 4, 6)
    planeten.add(solaria)
    val ktaris = Planet("Ktaris", false, 10, 3)
    planeten.add(ktaris)

    val gemuese = Ladung("Gemuese", 10)
    val nanobots = Ladung("Nanobots", 15)
    val antimaterieWaffen = Ladung("Antimaterie-Waffen", 125)
    val medizin = Ladung("Medizin", 13)

    ktaris.addLadung(gemuese)
    ktaris.addLadung(nanobots)
    solaria.addLadung(antimaterieWaffen)
    auroria.addLadung(medizin)

    val asteroidenFelder = mutableListOf(
        AsteroidenFeld(0, 5, 70), AsteroidenFeld(4, 3, 40)
    )

    return SpielStand(eos, raumschiffe, planeten, asteroidenFelder)
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
    spielStand: SpielStand,
    sichtweiteX: Int,
    sichtweiteY: Int,
): List<String> {
    val zeilen = mutableListOf<String>()
    zeilen.add("+" + "-".repeat(sichtweiteX * 2 + 1) + "+")

    for (dy in -sichtweiteX..sichtweiteX) {
        val y = spielStand.eos.posY + dy
        var zeile = "|"

        koordinatenLoop@ for (dx in -sichtweiteY..sichtweiteY) {
            val x = spielStand.eos.posX + dx
            val koordinaten = x to y

            if (spielStand.eos.koordinaten == koordinaten) {
                zeile += "x"
                continue
            }
            for (raumschiff in spielStand.raumschiffe) {
                if (raumschiff.koordinaten == koordinaten) {
                    zeile += ">"
                    continue@koordinatenLoop
                }
            }
            for (planet in spielStand.planeten) {
                if (planet.koordinaten == koordinaten) {
                    zeile += "O"
                    continue@koordinatenLoop
                }
            }
            for (feld in spielStand.asteroidenFelder) {
                if (feld.koordinaten == koordinaten) {
                    zeile += "#"
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
            auswahl = ConsoleHelper.printMenuOpt("Möchtest du Ladungen geben oder erhalten?", "Geben", "Erhalten")
        }

        when (auswahl) {
            0 -> break
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