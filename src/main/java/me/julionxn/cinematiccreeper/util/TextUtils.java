package me.julionxn.cinematiccreeper.util;

public class TextUtils {

    public static String idToLegibleText(String id){
        String[] parts = id.split(":")[1].split("_");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            builder.append(part.substring(0, 1).toUpperCase()).append(part.substring(1)).append(" ");
        }
        return builder.toString();
    }

}
