package me.julionxn.cinematiccreeper.util;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    public static String idToLegibleText(String id) {
        String[] parts = id.substring(id.indexOf(":") + 1).split("_");
        StringBuilder builder = new StringBuilder(id.length() - 9);
        for (String part : parts) {
            builder.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1))
                    .append(' ');
        }
        return builder.toString().trim();
    }

    public static String parseKeybind(KeyBinding keyBinding) {
        String text = keyBinding.getBoundKeyTranslationKey();
        return parseTranslationKey(text);
    }

    public static String parseKey(InputUtil.Key key){
        String text = key.getTranslationKey();
        return parseTranslationKey(text);
    }

    private static String parseTranslationKey(String translation){
        Pattern pattern = Pattern.compile("key\\.keyboard\\.(\\w+)");
        Matcher matcher = pattern.matcher(translation);
        if (matcher.matches()) {
            return matcher.group(1).toUpperCase();
        }
        return translation;
    }

}
