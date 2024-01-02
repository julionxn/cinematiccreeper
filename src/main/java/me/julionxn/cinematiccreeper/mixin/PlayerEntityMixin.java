package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.managers.paths.PlayerPathState;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerData {

    @Unique
    private PlayerPathState playerPathState = PlayerPathState.none();

    @Override
    public void cinematiccreeper$setPathState(PlayerPathState state) {
        this.playerPathState = state;
    }

    @Override
    public PlayerPathState cinematiccreeper$getPathState() {
        return playerPathState;
    }

}
