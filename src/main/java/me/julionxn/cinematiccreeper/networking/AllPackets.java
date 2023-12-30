package me.julionxn.cinematiccreeper.networking;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.networking.packets.C2S_ApplyPresetOptions;
import me.julionxn.cinematiccreeper.networking.packets.C2S_RemoveEntity;
import me.julionxn.cinematiccreeper.networking.packets.C2S_SpawnNpcPreset;
import me.julionxn.cinematiccreeper.networking.packets.C2S_SpawnPreset;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class AllPackets {

    public static final Identifier C2S_SPAWN_PRESET = of("spawn_preset");
    public static final Identifier C2S_SPAWN_NPC_PRESET = of("spawn_npc_preset");
    public static final Identifier C2S_APPLY_PRESET_OPTIONS = of("apply_preset_options");
    public static final Identifier C2S_REMOVE_ENTITY = of("remove_entity");

    private static Identifier of(String packetName){
        return new Identifier(CinematicCreeper.MOD_ID, packetName);
    }

    public static void registerS2CPackets(){

    }

    public static void registerC2SPackets(){
        c2s(C2S_SPAWN_PRESET, C2S_SpawnPreset::onServer);
        c2s(C2S_SPAWN_NPC_PRESET, C2S_SpawnNpcPreset::onServer);
        c2s(C2S_APPLY_PRESET_OPTIONS, C2S_ApplyPresetOptions::onServer);
        c2s(C2S_REMOVE_ENTITY, C2S_RemoveEntity::onServer);
    }

    private static void c2s(Identifier identifier, ServerPlayNetworking.PlayChannelHandler handler){
        ServerPlayNetworking.registerGlobalReceiver(identifier, handler);
    }

    private static void s2c(Identifier identifier, ClientPlayNetworking.PlayChannelHandler handler){
        ClientPlayNetworking.registerGlobalReceiver(identifier, handler);
    }

}
