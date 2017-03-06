package dev.nelson.mot.utils;

public class StringUtils {

    public static boolean isEmpty(String string){
        return string.trim().length() == 0;
    }

    public static String cleanString(String string){
        return string.replaceAll("[,.$€£₴\\sa-zA-Zа-яА-Я]", "");
    }
}
