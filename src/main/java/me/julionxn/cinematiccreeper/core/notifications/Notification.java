package me.julionxn.cinematiccreeper.core.notifications;

import net.minecraft.text.Text;

public record Notification(Type type, Text text) {

    public static final Notification NO_POINTS = new Notification(Type.WARNING, Text.translatable("messages.cinematiccreeper.no_points"));
    public static final Notification BLANK_ID = new Notification(Type.ERROR, Text.translatable("messages.cinematiccreeper.blank_id"));
    public static final Notification ALREADY_EXISTS = new Notification(Type.ERROR, Text.translatable("messages.cinematiccreeper.already_exists"));
    public static final Notification ID_NOT_FOUND = new Notification(Type.WARNING, Text.translatable("messages.cinematiccreeper.id_not_found"));
    public static final Notification CREATED_SUCCESSFULLY = new Notification(Type.OK, Text.translatable("messages.cinematiccreeper.created_successfully"));
    public static final Notification INVALID_NUMBER = new Notification(Type.ERROR, Text.translatable("messages.cinematiccreeper.invalid_number"));
    public static final Notification SAVED = new Notification(Type.OK, Text.translatable("messages.cinematiccreeper.saved"));
    public static final Notification BLANK_FIELD = new Notification(Type.ERROR, Text.translatable("messages.cinematiccreeper.blank_field"));

    public enum Type {
        WARNING, ERROR, OK
    }

}
