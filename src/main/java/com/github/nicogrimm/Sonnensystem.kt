package com.github.nicogrimm

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

    println("Sie fliegen das Raumschiff ${eos.name} gesteuert von ${eos.kapitaen?.name ?: "niemanden"}")

    val scanner = Scanner(System.`in`)
    val gameOver = false
    while (!gameOver) {
        println("Raumschiff Koordinaten: ${eos.koordinaten}")

        val richtung = richtungEingabe(scanner) ?: break

        eos.fliegen(richtung)

        if (eos.koordinaten == aurora.koordinaten) {
            println("Hier ist das Raumschiff ${aurora.name}")
        }

        for (planet in listOf(auroria, solaria, ktaris)) {
            if (eos.koordinaten == planet.koordinaten) {
                println("Hier ist der Planet ${planet.name}. Dieser Planet hat ${if (planet.atmosphaere) "eine" else "keine"} Atmosphaere.")
            }
        }
    }
}

fun richtungEingabe(scanner: Scanner): Richtung? {
    while (true) {
        print("Bewegung (w/a/s/d/q): ")
        val char = try {
            scanner.nextLine()[0]
        } catch (e: StringIndexOutOfBoundsException) {
            ' '
        }

        if (char == 'q') {
            return null;
        }

        val richtung = Richtung.fromChar(char)

        if (richtung == null) {
            println("FEHLER: Unbekannte Richtung: $char")
            continue
        }

        return richtung
    }
}
