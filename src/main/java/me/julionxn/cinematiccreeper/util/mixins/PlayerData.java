package me.julionxn.cinematiccreeper.util.mixins;

import me.julionxn.cinematiccreeper.managers.paths.PlayerPathState;

public interface PlayerData {

    void cinematiccreeper$setPathState(PlayerPathState state);
    PlayerPathState cinematiccreeper$getPathState();

}
