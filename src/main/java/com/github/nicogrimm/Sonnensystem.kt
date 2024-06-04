package com.github.nicogrimm

import java.util.Scanner

fun main() {
    val eos = Raumschiff("Eos Nova", 0, 2)
    val aurora = Raumschiff("Aurora Quest", 1, 3)

    println("Sie fliegen das Raumschiff ${eos.name}")

    val scanner = Scanner(System.`in`)
    val gameOver = false
    while (!gameOver) {
        println("Raumschiff Koordinaten: ${eos.koordinatenAlsString()}")

        val richtung = richtungEingabe(scanner)
        eos.fliegen(richtung)

        if (eos.koordinaten == aurora.koordinaten) {
            println("Hier ist das Raumschiff ${aurora.name}")
        }
    }
}

fun richtungEingabe(scanner: Scanner): Richtung {
    while (true) {
        print("Bewegung (w/a/s/d): ")
        val richtungChar = try {
            scanner.nextLine()[0]
        } catch (e: StringIndexOutOfBoundsException) {
            ' '
        }
        val richtung = Richtung.fromChar(richtungChar)

        if (richtung == null) {
            println("FEHLER: Unbekannte Richtung: $richtungChar")
            continue
        }

        return richtung
    }
}
