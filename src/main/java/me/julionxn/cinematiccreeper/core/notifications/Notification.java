package me.julionxn.cinematiccreeper.core.notifications;

public record Notification(Type type, String text) {

    public enum Type {
        WARNING, ERROR, OK
    }

}
