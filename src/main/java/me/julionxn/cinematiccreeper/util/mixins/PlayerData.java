package me.julionxn.cinematiccreeper.util.mixins;

import me.julionxn.cinematiccreeper.core.paths.PlayerPathHolder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface PlayerData {

    void cinematiccreeper$setPathHolder(PlayerPathHolder state);
    PlayerPathHolder cinematiccreeper$getPathHolder();
    String cinematiccreeper$getSkinUrl();
    @Nullable Identifier cinematiccreeper$getSkin();
    void cinematiccreeper$setSkinUrl(String url);
    void cinematiccreeper$setSkin(Identifier texture);

}
