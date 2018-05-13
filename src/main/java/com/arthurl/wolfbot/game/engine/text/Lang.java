package com.arthurl.wolfbot.game.engine.text;

import java.util.Locale;
import java.util.ResourceBundle;

public class Lang {

    private ResourceBundle message;

    public Lang(String lang) {
        final Locale currentLocale = new Locale(lang);
        message = ResourceBundle.getBundle("Messages", currentLocale);
    }

    public String get(String prop, String... str) {
        int index = 1;
        String text = get(prop);
        for (String st : str) {
            text = text.replace("{{" + index + "}}", st);
            index++;
        }
        return text;
    }

    public String get(String prop) {
        return message.getString(prop);
    }

    //public static String getM(String prop, String m){
    //return get(prop).replace("{mention}", m);
    //}
}
