package com.github.nicogrimm;

public enum Richtung {
    Oben, Rechts, Unten, Links;

    public static Richtung fromChar(char c) {
        return switch (c) {
            case 'w' -> Oben;
            case 'd' -> Rechts;
            case 's' -> Unten;
            case 'a' -> Links;
            default -> null;
        };
    }
}
