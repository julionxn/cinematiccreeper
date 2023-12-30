package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.managers.NpcsManager;
import me.julionxn.cinematiccreeper.managers.PresetsManager;
import me.julionxn.cinematiccreeper.managers.presets.PresetOptions;
import me.julionxn.cinematiccreeper.managers.presets.PresetOptionsHandlers;
import me.julionxn.cinematiccreeper.networking.AllPackets;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.util.mixins.NpcData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class NpcTypeMenu extends ExtendedScreen {

    private static final Identifier BACKGROUND = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/npc_type_bg.png");
    protected final PresetOptions presetOptions;
    protected final Entity entity;
    private final Consumer<PresetOptions> onReady;
    private final Runnable onCancel;
    private final String entityType;
    protected int x;
    protected int y;
    protected int width = 300;
    protected int height = 200;

    public NpcTypeMenu(String entityType, Consumer<PresetOptions> onReady, Runnable onCancel, PresetOptions presetOptions, @Nullable Entity entity) {
        super(Text.of("NpcTypeMenu"));
        this.entityType = entityType;
        this.onReady = onReady;
        this.onCancel = onCancel;
        this.presetOptions = presetOptions;
        this.entity = entity;
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

    private void addBasicButtons(int x, int y) {
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
            client.setScreen(new BasicTypeMenu(entityType, onReady, onCancel, presetOptions, entity));
        }).dimensions(x + 25, y - 20, 60, 20).build();
        addDrawableChild(commonButton);
        int nextX = x + 25 + 60;
        if (client == null) return;
        PlayerEntity player = client.player;
        if (player == null) return;
        World world = player.getWorld();
        if (NpcsManager.getInstance().isMobEntity(world, entityType)) {
            ButtonWidget mobButton = ButtonWidget.builder(Text.of("Mob"), button -> {
                if (client == null || client.currentScreen == null) return;
                if (client.currentScreen.getClass() == MobNpcTypeMenu.class) return;
                client.setScreen(new MobNpcTypeMenu(entityType, onReady, onCancel, presetOptions, entity));
            }).dimensions(nextX, y - 20, 60, 20).build();
            addDrawableChild(mobButton);
            nextX += 60;
        }
        if (NpcsManager.getInstance().isPathAwareEntity(world, entityType)) {
            ButtonWidget pathAwareButton = ButtonWidget.builder(Text.of("Path"), button -> {
                if (client == null || client.currentScreen == null) return;
                if (client.currentScreen.getClass() == PathAwareNpcTypeMenu.class) return;
                client.setScreen(new PathAwareNpcTypeMenu(entityType, onReady, onCancel, presetOptions, entity));
            }).dimensions(nextX, y - 20, 60, 20).build();
            addDrawableChild(pathAwareButton);
        }
        if (entity == null) return;
        ButtonWidget removeButton = ButtonWidget.builder(Text.of("E"), button -> {
            String id = ((NpcData) entity).cinematiccreeper$getId();
            PresetsManager.getInstance().getPresetWithId(id).ifPresent(preset -> {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeUuid(entity.getUuid());
                ClientPlayNetworking.send(AllPackets.C2S_REMOVE_ENTITY, buf);
            });
        }).dimensions(x + width, y + 20, 20, 20).build();
        addDrawableChild(removeButton);
        ButtonWidget resetButton = ButtonWidget.builder(Text.of("R"), button -> {
            String id = ((NpcData) entity).cinematiccreeper$getId();
            PresetsManager.getInstance().getPresetWithId(id).ifPresent(preset -> {
                PresetOptions presetOptions1 = preset.getOptions();
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeUuid(entity.getUuid());
                buf.writeBoolean(true);
                PresetOptionsHandlers.addToBuf(buf, presetOptions1);
                ClientPlayNetworking.send(AllPackets.C2S_APPLY_PRESET_OPTIONS, buf);
            });
        }).dimensions(x + width, y + 40, 20, 20).build();
        addDrawableChild(resetButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderInGameBackground(DrawContext context) {
        super.renderInGameBackground(context);
        x = (windowWidth / 2) - width / 2;
        y = (windowHeight / 2) - height / 2;
        context.setShaderColor(1f, 1f, 1f, 1f);
        context.drawTexture(BACKGROUND, x, y, 0, 0, 300, 200, 300, 200);
        context.fill(x + 3, y + 3, x + width - 3, y + height - 3, 0, 0x1fffffff);

    }
}
