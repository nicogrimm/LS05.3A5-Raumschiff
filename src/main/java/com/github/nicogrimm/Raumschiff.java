package com.github.nicogrimm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Raumschiff {
    private String name;
    private int posX;
    private int posY;

    public void fliegen(Richtung richtung) {
        switch (richtung) {
            case Oben -> this.posY -= 1;
            case Links -> this.posX -= 1;
            case Unten -> this.posY += 1;
            case Rechts -> this.posX += 1;
        }
    }

    public int[] getKoordianten() {
        return new int[]{this.posX, this.posY};
    }

    public void koordinatenAusgeben() {
        System.out.printf("(%d, %d)", posX, posY);
    }
}