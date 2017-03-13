package dev.nelson.mot.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.nelson.mot.payment.Payment;

public class StringUtils {

    public static boolean isEmpty(String string){
        return string.trim().length() == 0;
    }

    public static String cleanString(String string){
        return string.replaceAll("[,.$€£₴\\sa-zA-Zа-яА-Я]", "");
    }

    public static boolean isLastCharacterDigit(String string){
        String lastCharacter = string.substring(string.length()-1, string.length());
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(lastCharacter);
        return matcher.find();
    }

    public static String formattedCost(long cost){
        Locale myLocale = LocaleUtils.getLocaleForChoosenCurrency();
        BigDecimal costValue = new BigDecimal(cost);
        BigDecimal parsed = costValue.setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        return NumberFormat.getCurrencyInstance(myLocale).format(parsed);
    }

    public static String makeCostNegative(String cost){
        return "- " + cost;
    }
}
