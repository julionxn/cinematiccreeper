package me.julionxn.cinematiccreeper.core.notifications;

import net.minecraft.text.Text;

public record Notification(Type type, Text text) {

    public enum Type {
        WARNING, ERROR, OK
    }

}
