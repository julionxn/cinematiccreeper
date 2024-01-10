package me.julionxn.cinematiccreeper.screen.gui.screens.camera;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.camera.paths.PathType;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NewCameraRecordingMenu extends Screen {

    private static final Identifier POINT_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "point.png");
    private PathType type = PathType.LINEAR;

    public NewCameraRecordingMenu() {
        super(Text.of("NewRecording"));
    }

    @Override
    protected void init() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        int centerX = client.getWindow().getScaledWidth() / 2;
        int centerY = client.getWindow().getScaledHeight() / 2;
        TextFieldWidget idTextField = new TextFieldWidget(
                client.textRenderer,
                centerX - 120, centerY - 20,
                100, 20,
                Text.of("Id")
        );
        addDrawableChild(idTextField);
        ButtonWidget linearButton = ButtonWidget.builder(Text.of("LINEAR"), button ->
                type = PathType.LINEAR
        ).dimensions(centerX + 20, centerY - 30, 100, 20).build();
        ButtonWidget catmullButton = ButtonWidget.builder(Text.of("CATMULL"), button ->
                type = PathType.CATMULL
        ).dimensions(centerX + 20, centerY - 10, 100, 20).build();
        ButtonWidget bSplineButton = ButtonWidget.builder(Text.of("BSPLINE"), button ->
                type = PathType.BSPLINE
        ).dimensions(centerX + 20, centerY + 10, 100, 20).build();
        addDrawableChild(linearButton);
        addDrawableChild(catmullButton);
        addDrawableChild(bSplineButton);
        ButtonWidget createButton = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.create"), button -> {
            CameraManager manager = CameraManager.getInstance();
            String id = idTextField.getText();
            if (id.replace(" ", "").isEmpty()){
                NotificationManager.getInstance().add(Notification.BLANK_ID);
                return;
            }
            if (manager.getCameraRecordings().stream().anyMatch(recording -> recording.id.equals(id))){
                NotificationManager.getInstance().add(Notification.ALREADY_EXISTS);
                return;
            }
            manager.startRecording(id, type);
            close();
        }).dimensions(centerX - 120, centerY, 100, 20).build();
        addDrawableChild(createButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int x = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int y = type == PathType.LINEAR ? centerY - 30 : type == PathType.CATMULL ? centerY - 10 : centerY + 10;
        context.drawTexture(POINT_TEXTURE, x, y, 0, 0, 0, 20, 20, 20, 20);
    }

}
