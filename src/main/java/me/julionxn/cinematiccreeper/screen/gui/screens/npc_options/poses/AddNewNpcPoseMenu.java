package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options.poses;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.poses.PoseType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AddNewNpcPoseMenu extends Screen {

    private static final Identifier POINT_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "point.png");
    private final Screen previousScreen;
    private PoseType poseType;

    public AddNewNpcPoseMenu(Screen previousScreen) {
        super(Text.of("AddNewNpcPose"));
        this.previousScreen = previousScreen;
    }

    @Override
    protected void init() {
        super.init();
        if (client == null) return;
        PlayerEntity player = client.player;
        if (player == null) return;
        int centerX = client.getWindow().getScaledWidth() / 2;
        int centerY = client.getWindow().getScaledHeight() / 2;
        TextFieldWidget idField = new TextFieldWidget(
                client.textRenderer, centerX - 120, centerY, 100, 20, Text.of("PoseId")
        );
        addDrawableChild(idField);
        ButtonWidget staticButton = ButtonWidget.builder(Text.of("Estática"), button ->
                        poseType = PoseType.STATIC
                ).dimensions(centerX + 20, centerY + 20, 100, 20).build();
        ButtonWidget dynamicButton = ButtonWidget.builder(Text.of("Dinámica"), button ->
                        poseType = PoseType.DYNAMIC
                ).dimensions(centerX + 20, centerY, 100, 20).build();
        addDrawableChild(staticButton);
        addDrawableChild(dynamicButton);
        ButtonWidget createButton = ButtonWidget.builder(Text.of("Crear"), button -> {
            if (poseType == PoseType.STATIC){
                client.setScreen(new StaticPoseMenu(previousScreen, idField.getText()));
            } else {
                client.setScreen(new DynamicPoseMenu(previousScreen, idField.getText()));
            }
        }).dimensions(centerX - 120, centerY + 20, 100, 20).build();
        addDrawableChild(createButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int x = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int y = poseType == PoseType.STATIC ? centerY + 20 : centerY;
        context.drawTexture(POINT_TEXTURE, x, y, 0, 0, 0, 20, 20, 20, 20);
    }

}
