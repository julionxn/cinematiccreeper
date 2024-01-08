package me.julionxn.cinematiccreeper.keybinds;

public enum PressModifier {
    NONE, SHIFT, CTRL;

    public static PressModifier get(int modifier){
        return switch (modifier){
            case 0 -> NONE;
            case 1 -> SHIFT;
            default -> CTRL;
        };
    }

}
