package me.julionxn.cinematiccreeper.mixin;

import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;",
    at = @At("TAIL"), cancellable = true)
    private void texture(AbstractClientPlayerEntity abstractClientPlayerEntity, CallbackInfoReturnable<Identifier> cir){
        Identifier texture = ((PlayerData) abstractClientPlayerEntity).cinematiccreeper$getSkin();
        if (texture != null){
            cir.setReturnValue(texture);
        }
    }

}
