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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class NpcTypeMenu extends ExtendedScreen {

    private static final Identifier BACKGROUND = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/npc_type_bg.png");
    protected final PresetOptions presetOptions;
    protected final Entity entity;
    private final Consumer<PresetOptions> onReady;
    private final Runnable onCancel;
    protected final String entityType;
    protected int x;
    protected int y;
    protected int width = 300;
    protected int height = 200;
    private final List<Tab> tabs = new ArrayList<>();

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

        addTab("Básico", (buttonWidget, minecraftClient) -> {
            if (minecraftClient.currentScreen == null) return;
            if (minecraftClient.currentScreen.getClass() == BasicTypeMenu.class) return;
            minecraftClient.setScreen(new BasicTypeMenu(entityType, onReady, onCancel, presetOptions, entity));
        }, (string, minecraftClient) -> true);

        addTab("Mob", (buttonWidget, minecraftClient) -> {
            if (minecraftClient.currentScreen == null) return;
            if (minecraftClient.currentScreen.getClass() == MobNpcTypeMenu.class) return;
            minecraftClient.setScreen(new MobNpcTypeMenu(entityType, onReady, onCancel, presetOptions, entity));
        }, (string, minecraftClient) -> {
            PlayerEntity player = minecraftClient.player;
            if (player == null) return false;
            World world = player.getWorld();
            return NpcsManager.getInstance().isMobEntity(world, string);
        });

        addTab("Path", (buttonWidget, minecraftClient) -> {
            if (minecraftClient.currentScreen == null) return;
            if (minecraftClient.currentScreen.getClass() == PathAwareNpcTypeMenu.class) return;
            minecraftClient.setScreen(new PathAwareNpcTypeMenu(entityType, onReady, onCancel, presetOptions, entity));
        }, (string, minecraftClient) -> {
            PlayerEntity player = minecraftClient.player;
            if (player == null) return false;
            World world = player.getWorld();
            return NpcsManager.getInstance().isPathAwareEntity(world, string);
        });

        if (client == null) return;
        List<Tab> filteredTabs = tabs.stream().filter(tab -> tab.predicate.test(entityType, client)).toList();
        int startingX = x + 25;
        int tabWidth = (width - 50) / filteredTabs.size();
        for (int i = 0; i < filteredTabs.size(); i++) {
            Tab tab = filteredTabs.get(i);
            ButtonWidget buttonWidget = ButtonWidget.builder(Text.of(tab.text), button -> tab.onClick.accept(button, client))
                    .dimensions(startingX + (i * tabWidth), y - 20, tabWidth, 20).build();
            addDrawableChild(buttonWidget);
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

    protected void addTab(String text, BiConsumer<ButtonWidget, MinecraftClient> onClick, BiPredicate<String, MinecraftClient> predicate){
        tabs.add(new Tab(text, onClick, predicate));
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

    private record Tab(String text, BiConsumer<ButtonWidget, MinecraftClient> onClick, BiPredicate<String, MinecraftClient> predicate) {

    }
}
