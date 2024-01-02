package me.julionxn.cinematiccreeper.managers.paths;

public record PlayerPathState(me.julionxn.cinematiccreeper.managers.paths.PlayerPathState.State state, Path path) {

    private static final PlayerPathState NONE = new PlayerPathState(State.NONE, null);

    public static PlayerPathState none() {
        return NONE;
    }

    public enum State {
        NONE, ADDING, RECORDING
    }

}
