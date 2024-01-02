package me.julionxn.cinematiccreeper.managers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

@Environment(EnvType.CLIENT)
public class CameraManager {

    private boolean active;
    private boolean moving;
    private Double fov;

    public boolean isMoving(){
        return moving;
    }

    public void setMoving(boolean moving){
        this.moving = moving;
    }

    public boolean isActive(){
        return active;
    }

    public void toggleActive(){
        active = !active;
    }

    public void setFov(double fov){
        if (this.fov <= 0) return;
        this.fov = fov;
    }

    public double getFov(){
        if (fov == null){
            fov = MinecraftClient.getInstance().options.getFov().getValue().doubleValue();
        }
        return fov;
    }

    private static class SingletonHolder {
        private static final CameraManager INSTANCE = new CameraManager();
    }

    public static CameraManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

}
