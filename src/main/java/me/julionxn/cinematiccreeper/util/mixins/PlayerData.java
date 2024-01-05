package me.julionxn.cinematiccreeper.util.mixins;

import me.julionxn.cinematiccreeper.core.paths.PlayerPathHolder;

public interface PlayerData {

    void cinematiccreeper$setPathHolder(PlayerPathHolder state);

    PlayerPathHolder cinematiccreeper$getPathHolder();

}
