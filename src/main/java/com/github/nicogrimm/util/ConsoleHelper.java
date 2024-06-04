package com.github.nicogrimm.util;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Trutz
 * @version 240416 (modifiziert)
 */

public class ConsoleHelper {

    private static Scanner s;

    private static Scanner getScanner(){
        if (s == null) {
            s = new Scanner(System.in);
        }
        return s;
    }

    /**
     * Print as header formated
     * @param txt to show
     */
    public static void header(String txt){
        System.out.println("-".repeat(txt.length()+4));
        System.out.println("# "+txt+" #");
        System.out.println("-".repeat(txt.length()+4));
    }

    public static String input(String txt){
        System.out.printf("%s: ",txt);
        return getScanner().nextLine();
    }
    public static int inputInt(String txt){
        System.out.printf("%s: ",txt);
        try {
            return new Scanner(System.in).nextInt();
        } catch (Throwable t){
            System.out.printf("Bitte Zahl eingeben! (%s: %s)",t.getClass().getSimpleName(), t.getLocalizedMessage());
            System.out.println();
            return inputInt(txt);
        }
    }

    public static int inputInt(String txt, int min, int max){
        int erg = inputInt(txt+" ["+min+"-"+max+"]");
        if (erg >= min && erg <= max)
            return erg;
        else {
            System.out.printf("Bitte Zahl zwischen %s und %s eingeben!%n",min, max);
            return inputInt(txt, min, max);
        }
    }

    public static void printMenuElement(int count, String txt) {
        System.out.printf("[%2s] %s%n",count,txt);
    }

    /**
     * Generate a menu for this Array menu
     * @param header
     * @param txt
     * @return
     */
    public static int printMenu(String header, ArrayList txt) {
        return printMenu(header, txt.toArray());
    }

    /**
     * Generate a menu for this Array
     * @param header
     * @param txt
     * @return
     */
    public static int printMenu(String header, Object... txt) {
        header(header);
        int c = 1;
        for (Object s: txt) {
            printMenuElement(c++,s.toString());
        }
        return inputInt("Wählen Sie", 1, txt.length);
    }

    /**
     * Generate a cancellable menu for this Array. A return value of 0 means nothing has been selected.
     * @param header
     * @param txt
     * @return
     */
    public static int printMenuOpt(String header, ArrayList txt) {
        return printMenuOpt(header, txt.toArray());
    }

    /**
     * Generate a cancellable menu for this Array. A return value of 0 means nothing has been selected.
     * @param header
     * @param txt
     * @return
     */
    public static int printMenuOpt(String header, Object... txt) {
        header(header);
        printMenuElement(0,"Auswahl abbrechen");
        int c = 1;
        for (Object s: txt) {
            printMenuElement(c++,s.toString());
        }
        return inputInt("Wählen Sie", 0, txt.length);
    }

    /**
     * Print a message normal or as error, if success is false
     * @param txt, text to print
     * @param success, if success print normal, otherwise as error
     */
    public static void valid(String txt, boolean success){
        if (success) {
            System.out.println("> Erfolgreich: " + txt);
        } else {
            System.err.println("> Fehler: " + txt);
        }
    }
}