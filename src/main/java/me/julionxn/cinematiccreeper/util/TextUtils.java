package me.julionxn.cinematiccreeper.util;

public class TextUtils {

    public static String idToLegibleText(String id){
        String[] parts = id.substring(id.indexOf(":") + 1).split("_");
        StringBuilder builder = new StringBuilder(id.length() - 9);
        for (String part : parts) {
            builder.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1))
                    .append(' ');
        }
        return builder.toString().trim();
    }

}
