package com.github.nicogrimm

import kotlin.math.min
import kotlin.random.Random

class Raumschiff(
    val name: String,
    var posX: Int = 0,
    var posY: Int = 0,
    kapitaen: Kapitaen? = null,
    maxIntigritaet: Int = 100,
    maxEnergieSchild: Int = 100,
    var energieVersorgung: Int = 100,
    var manoevrierFaehigkeit: Int = 50,
    var waffenstaerke: Int = 100
) : LadungsBesitzer {
    var kapitaen: Kapitaen? = null
        set(value) {
            kapitaen?.raumschiff = null

            field = value
            value?.raumschiff = this
        }

    init {
        this.kapitaen = kapitaen
    }

    var integritaet = maxIntigritaet
    var maxIntegritaet: Int = maxIntigritaet
        set(value) {
            if (value > field) {
                val delta = value - field
                integritaet += delta
            } else if (value < integritaet) {
                integritaet = value
            }
            field = value
        }

    var energieSchild = maxEnergieSchild
    var maxEnergieSchild: Int = maxEnergieSchild
        set(value) {
            if (value > field) {
                val delta = value - field
                energieSchild += delta
            } else if (value < energieSchild) {
                energieSchild = value
            }
            field = value
        }

    fun fliegen(richtung: Richtung) {
        when (richtung) {
            Richtung.Oben -> this.posY -= 1
            Richtung.Links -> this.posX -= 1
            Richtung.Unten -> this.posY += 1
            Richtung.Rechts -> this.posX += 1
        }
    }

    val koordinaten: Pair<Int, Int>
        get() = this.posX to this.posY

    override val ladungen: MutableList<Ladung> = mutableListOf()

    override fun addLadung(ladung: Ladung) {
        ladung.besitzer?.removeLadung(ladung)

        ladung.besitzer = this
        ladungen.add(ladung)
    }

    override fun removeLadung(ladung: Ladung) {
        ladung.besitzer = null
        ladungen.remove(ladung)
    }

    // Ein Raumschiff erreicht 100% Ausweichchance bei etwa 150 manoevrierFaehigkeit
    fun ausweichChance() = (0.022 * manoevrierFaehigkeit) / (0.01 * manoevrierFaehigkeit + 1.8)
    fun asteroidenAusweichChance() = (0.012 * manoevrierFaehigkeit) / (0.01 * manoevrierFaehigkeit + 0.5) + 0.1

    fun angreifen(gegner: Raumschiff) {
        if (gegner.integritaet > 0 && Random.Default.nextDouble(0.1, 1.0) <= gegner.ausweichChance()) {
            println("Angriff hat nicht getroffen")
            return
        }

        // Kapitänmodifikator: 1..10 => 0.8..1.2, fällt auf 0.88 zurück ohne Kapitän
        // multipliziert mit zufälliger Zahl: 0.9..1.2
        val schadenModifikator =
            (((kapitaen?.erfahrung ?: 3) - 1) / 9 * 0.4 + 0.8) * Random.Default.nextDouble(0.9, 1.2)

        gegner.schadenNehmen((waffenstaerke * schadenModifikator).toInt())
    }

    fun schadenNehmen(schaden: Int) {
        var restlicherSchaden = schaden
        if (this.energieSchild > 0) {
            val energieSchildSchaden = min(restlicherSchaden, this.energieSchild)
            restlicherSchaden -= energieSchildSchaden
            this.energieSchild -= energieSchildSchaden

            println("Das Energieschild von ${this.name} hat $energieSchildSchaden Schaden genommen")

            if (this.energieSchild == 0) {
                println("Das Energieschild von ${this.name} wurde zerstoert")
            }
        }
        if (restlicherSchaden > 0) {
            val integritaetSchaden = min(this.integritaet, restlicherSchaden)
            this.integritaet -= integritaetSchaden
            println("Die Integritaet von ${this.name} hat $integritaetSchaden Schaden genommen")

            if (this.integritaet == 0) {
                println("Die Integritaet von ${this.name} wurde zerstoert")
            }
        }
    }
}