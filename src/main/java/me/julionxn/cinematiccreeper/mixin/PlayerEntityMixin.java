package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.managers.paths.PlayerPathHolder;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerData {

    @Unique
    private PlayerPathHolder playerPathHolder = PlayerPathHolder.none();

    @Override
    public void cinematiccreeper$setPathHolder(PlayerPathHolder state) {
        this.playerPathHolder = state;
    }

    @Override
    public PlayerPathHolder cinematiccreeper$getPathHolder() {
        return playerPathHolder;
    }

}
