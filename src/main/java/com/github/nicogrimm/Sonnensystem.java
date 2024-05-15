package com.github.nicogrimm;

import java.util.Arrays;
import java.util.Scanner;

public class Sonnensystem {
    public static void main(String[] args) {
        Raumschiff eos = new Raumschiff("Eos Nova", 0, 2);
        Raumschiff aurora = new Raumschiff("Aurora Quest", 1, 3);

        System.out.println("Sie fliegen das Raumschiff " + eos.getName());

        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;
        while (!gameOver) {
            System.out.print("Raumschiff Koordinaten: ");
            eos.koordinatenAusgeben();
            System.out.println();

            Richtung richtung = richtungEingabe(scanner);
            eos.fliegen(richtung);

            if (Arrays.equals(eos.getKoordianten(), aurora.getKoordianten())) {
                System.out.println("Hier ist das Raumschiff " + aurora.getName());
            }
        }
    }

    public static Richtung richtungEingabe(Scanner scanner) {
        while (true) {
            System.out.print("Bewegung (w/a/s/d): ");

            char richtungChar;
            try {
                richtungChar = scanner.nextLine().charAt(0);
            } catch (StringIndexOutOfBoundsException e) {
                richtungChar = ' ';
            }
            Richtung richtung = Richtung.fromChar(richtungChar);

            if (richtung == null) {
                System.out.println("FEHLER: Unbekannte Richtung: " + richtungChar);
                continue;
            }

            return richtung;
        }
    }
}
