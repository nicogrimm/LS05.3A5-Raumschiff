package com.github.nicogrimm

import java.util.Scanner

fun main() {
    val alexia = Kapitaen("Alexia Nova", 8, 6)
    val zenith = Kapitaen("Zenith Nightfall", 7, 8)

    alexia.name = "Alexia Starlight Nova"

    val eos = Raumschiff("Eos Nova", 0, 2, kapitaen = alexia)
    val aurora = Raumschiff("Aurora Quest", 1, 3, kapitaen = zenith)

    println("Sie fliegen das Raumschiff ${eos.name} gesteuert von ${eos.kapitaen?.name ?: "niemanden"}")

    val scanner = Scanner(System.`in`)
    val gameOver = false
    while (!gameOver) {
        println("Raumschiff Koordinaten: ${eos.koordinatenAlsString()}")

        val richtung = richtungEingabe(scanner) ?: break

        eos.fliegen(richtung)

        if (eos.koordinaten == aurora.koordinaten) {
            println("Hier ist das Raumschiff ${aurora.name}")
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
