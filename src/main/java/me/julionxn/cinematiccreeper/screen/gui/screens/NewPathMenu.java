package me.julionxn.cinematiccreeper.screen.gui.screens;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.managers.paths.Path;
import me.julionxn.cinematiccreeper.managers.paths.PlayerPathState;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NewPathMenu extends Screen {

    private final int entityId;
    private final PlayerPathState.State state;
    private Path.Type type = Path.Type.LOOP;

    public NewPathMenu(int entityId, PlayerPathState.State state) {
        super(Text.of("NewPath"));
        this.entityId = entityId;
        this.state = state;
    }

    @Override
    protected void init() {
        super.init();
        if (client == null) return;
        PlayerEntity player = client.player;
        if (player == null) return;
        int centerX = client.getWindow().getScaledWidth() / 2;
        int centerY = client.getWindow().getScaledHeight() / 2;
        TextFieldWidget idTextField = new TextFieldWidget(
                client.textRenderer,
                centerX - 120,
                centerY,
                100, 20,
                Text.of("Id")
        );
        addDrawableChild(idTextField);
        ButtonWidget pingPongButton = ButtonWidget.builder(Text.of("Ping Pong"), button ->
                        type = Path.Type.PING_PONG
                )
                .dimensions(centerX + 20, centerY + 20, 100, 20).build();
        ButtonWidget loopButton = ButtonWidget.builder(Text.of("Loop"), button ->
                        type = Path.Type.LOOP
                )
                .dimensions(centerX + 20, centerY, 100, 20).build();
        addDrawableChild(pingPongButton);
        addDrawableChild(loopButton);
        ButtonWidget addButton = ButtonWidget.builder(Text.of("Empezar"), button -> {
            ((PlayerData) player).cinematiccreeper$setPathState(new PlayerPathState(state,
                    new Path(idTextField.getText(), type, entityId)
            ));
            close();
        }).dimensions(centerX - 120, centerY + 20, 100, 20).build();
        addDrawableChild(addButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int x = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int y = type == Path.Type.PING_PONG ? centerY + 20 : centerY;
        context.drawTexture(new Identifier(CinematicCreeper.MOD_ID, "point.png"),
                x, y, 0, 0, 0, 20, 20, 20, 20
        );
    }
}
