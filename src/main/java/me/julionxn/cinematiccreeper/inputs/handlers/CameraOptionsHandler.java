package me.julionxn.cinematiccreeper.inputs.handlers;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.inputs.InputAction;
import me.julionxn.cinematiccreeper.inputs.InputHandler;
import me.julionxn.cinematiccreeper.inputs.Keybindings;
import me.julionxn.cinematiccreeper.screen.gui.screens.camera.SettingsCameraMenu;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class CameraOptionsHandler extends InputHandler {

    private static final Identifier CAMERA_MODE_ICON = new Identifier(CinematicCreeper.MOD_ID, "textures/hud/camera_mode_icon.png");
    private static final Identifier PLAYER_MODE_ICON = new Identifier(CinematicCreeper.MOD_ID, "textures/hud/player_mode_icon.png");

    @Override
    public void init() {
        setPredicate(client -> !CameraManager.getInstance().isRecording());
        addPressAction(new InputAction(Keybindings.firstAction, Text.translatable("camera.cinematiccreeper.detach_camera"),
                (client, modifier) -> {
                    CameraManager.State newState = CameraManager.getInstance().isActive() ? CameraManager.State.NONE : CameraManager.State.STATIC;
                    if (newState == CameraManager.State.STATIC) {
                        PlayerEntity player = client.player;
                        if (player == null) return;
                        Vec3d pos = player.getPos();
                        CameraManager.getInstance().setAnchorValues(pos.x, pos.y + 1.5, pos.z, player.getYaw(), player.getPitch());
                    }
                    CameraManager.getInstance().setState(newState);
                }));
        addPressAction(new InputAction(Keybindings.secondAction, Text.translatable("camera.cinematiccreeper.control_mode"),
                (client, modifier) -> {
                    CameraManager.State newState = CameraManager.getInstance().getState() == CameraManager.State.MOVING ? CameraManager.State.STATIC : CameraManager.State.MOVING;
                    CameraManager.getInstance().setState(newState);
                }, () -> CameraManager.getInstance().isActive()));
        addPressAction(new InputAction(GLFW.GLFW_KEY_O, Text.translatable("camera.cinematiccreeper.camera_settings"),
                (client, pressModifier) -> client.setScreen(new SettingsCameraMenu()),
                () -> CameraManager.getInstance().isActive()));
    }

    @Override
    public boolean shouldRender() {
        return CameraManager.getInstance().isActive() && CameraManager.getInstance().getSettings().showOptions();
    }

    @Override
    public void render(DrawContext context) {
        int width = context.getScaledWindowWidth();
        CameraManager.State state = CameraManager.getInstance().getState();
        if (state == CameraManager.State.NONE) return;
        Identifier texture = state == CameraManager.State.STATIC ? PLAYER_MODE_ICON : CAMERA_MODE_ICON;
        context.drawTexture(texture, width - 72, 8, 0, 0, 0, 64, 64, 64, 64);
    }

    @Override
    public boolean shouldCancelNext() {
        return false;
    }
}
