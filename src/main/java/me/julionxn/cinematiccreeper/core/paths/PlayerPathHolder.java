package me.julionxn.cinematiccreeper.core.paths;

public record PlayerPathHolder(PlayerPathHolder.State state, Path path) {

    private static final PlayerPathHolder NONE = new PlayerPathHolder(State.NONE, null);

    public static PlayerPathHolder none() {
        return NONE;
    }

    public enum State {
        NONE, ADDING, RECORDING
    }

}
