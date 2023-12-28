package me.julionxn.cinematiccreeper.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class ExampleMixin {
	@Shadow public abstract float getMovementSpeed();

	@Inject(at = @At("HEAD"), method = "tick")
	private void init(CallbackInfo info) {
		System.out.println(getMovementSpeed());
	}
}