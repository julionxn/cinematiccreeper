package me.julionxn.cinematiccreeper.keybinds;

public enum PressState {
    PRESS, HOLD, RELEASE;

    public static PressState get(int action){
        return switch (action){
            case 1 -> PRESS;
            case 2 -> HOLD;
            default -> RELEASE;
        };
    }

}
