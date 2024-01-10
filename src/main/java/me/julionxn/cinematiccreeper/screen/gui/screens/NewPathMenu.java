package me.julionxn.cinematiccreeper.screen.gui.screens;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.core.paths.Path;
import me.julionxn.cinematiccreeper.core.paths.PlayerPathHolder;
import me.julionxn.cinematiccreeper.util.mixins.PathAwareData;
import me.julionxn.cinematiccreeper.util.mixins.PlayerData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NewPathMenu extends Screen {

    private final int entityId;
    private final PlayerPathHolder.State state;
    private Path.Type type = Path.Type.LOOP;

    public NewPathMenu(int entityId, PlayerPathHolder.State state) {
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
        ButtonWidget pingPongButton = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.ping_pong"), button ->
                        type = Path.Type.PING_PONG
                )
                .dimensions(centerX + 20, centerY + 20, 100, 20).build();
        ButtonWidget loopButton = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.loop"), button ->
                        type = Path.Type.LOOP
                )
                .dimensions(centerX + 20, centerY, 100, 20).build();
        addDrawableChild(pingPongButton);
        addDrawableChild(loopButton);
        ButtonWidget addButton = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.start"), button -> {
            String id = idTextField.getText();
            if (id.replace(" ", "").isEmpty()) {
                NotificationManager.getInstance().add(Notification.BLANK_ID);
                return;
            }
            Entity entity = player.getWorld().getEntityById(entityId);
            if (entity == null) return;
            if (((PathAwareData) entity).cinematiccreeper$getPaths().stream().anyMatch(path -> path.getId().equals(id))){
                NotificationManager.getInstance().add(Notification.ALREADY_EXISTS);
                return;
            }
            ((PlayerData) player).cinematiccreeper$setPathHolder(new PlayerPathHolder(state,
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
