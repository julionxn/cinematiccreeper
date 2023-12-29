package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.managers.NpcsManager;
import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.function.Consumer;

public abstract class NpcTypeMenu extends ExtendedScreen {

    protected int x;
    protected int y;
    protected int width = 300;
    protected int height = 200;
    protected final PresetOptions presetOptions;
    private final Consumer<PresetOptions> onReady;
    private final Runnable onCancel;
    private final String entityType;

    public NpcTypeMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions) {
        super(Text.of("NpcTypeMenu"));
        this.entityType = entityType;
        this.onReady = onReady;
        this.onCancel = onCancel;
        this.presetOptions = presetOptions;
    }

    @Override
    public void addWidgets() {
        x = (windowWidth / 2) - width / 2;
        y = (windowHeight / 2) - height / 2;
    }

    @Override
    public void addDrawables() {
        x = (windowWidth / 2) - width / 2;
        y = (windowHeight / 2) - height / 2;
        addBasicButtons(x, y);
    }

    private void addBasicButtons(int x, int y){
        ButtonWidget cancelButton = ButtonWidget.builder(Text.of("Cancelar"), button -> {
            onCancel.run();
        }).dimensions(x + 20, y + height, 100, 20).build();
        addDrawableChild(cancelButton);
        ButtonWidget saveButton = ButtonWidget.builder(Text.of("Guardar"), button -> {
            onReady.accept(presetOptions);
        }).dimensions(x + width - 120, y + height, 100, 20).build();
        addDrawableChild(saveButton);
        ButtonWidget commonButton = ButtonWidget.builder(Text.of("Básico"), button -> {
            if (client == null || client.currentScreen == null) return;
            if (client.currentScreen.getClass() == BasicTypeMenu.class) return;
            client.setScreen(new BasicTypeMenu(entityType, onReady, onCancel, presetOptions));
        }).dimensions(x + 25, y - 20, 60, 20).build();
        addDrawableChild(commonButton);

        int nextX = x + 25 + 60;
        if (client == null) return;
        PlayerEntity player = client.player;
        if (player == null) return;
        World world = player.getWorld();
        if (NpcsManager.getInstance().isMobEntity(world, entityType)){
            ButtonWidget mobButton = ButtonWidget.builder(Text.of("Mob"), button -> {
                if (client == null || client.currentScreen == null) return;
                if (client.currentScreen.getClass() == MobNpcTypeMenu.class) return;
                client.setScreen(new MobNpcTypeMenu(entityType, onReady, onCancel, presetOptions));
            }).dimensions(nextX, y - 20, 60, 20).build();
            addDrawableChild(mobButton);
            nextX += 60;
        }
        if (NpcsManager.getInstance().isPathAwareEntity(world, entityType)){
            ButtonWidget pathAwareButton = ButtonWidget.builder(Text.of("Path"), button -> {
                if (client == null || client.currentScreen == null) return;
                if (client.currentScreen.getClass() == PathAwareNpcTypeMenu.class) return;
                client.setScreen(new PathAwareNpcTypeMenu(entityType, onReady, onCancel, presetOptions));
            }).dimensions(nextX, y - 20, 60, 20).build();
            addDrawableChild(pathAwareButton);
        }
    }

}
