package model;

public class WordUtils {
    public static String format(String word) {
        if (word == null) return "";
        return word.trim().toUpperCase();
    }
}
