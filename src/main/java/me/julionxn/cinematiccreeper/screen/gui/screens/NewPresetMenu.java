package me.julionxn.cinematiccreeper.screen.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import me.julionxn.cinematiccreeper.core.managers.NpcsManager;
import me.julionxn.cinematiccreeper.core.managers.PresetsManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.core.presets.Preset;
import me.julionxn.cinematiccreeper.core.presets.PresetOptions;
import me.julionxn.cinematiccreeper.core.skins.CachedSkin;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.DefaultedTextField;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.ScrollWidget;
import me.julionxn.cinematiccreeper.screen.gui.screens.npc_options.BasicTypeMenu;
import me.julionxn.cinematiccreeper.screen.gui.screens.skins.SkinClosetMenu;
import me.julionxn.cinematiccreeper.util.MathHelper;
import me.julionxn.cinematiccreeper.util.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;

import java.util.ArrayList;
import java.util.List;

public class NewPresetMenu extends ExtendedScreen {

    private final List<ScrollWidget.ScrollItem> items = new ArrayList<>();
    private List<ScrollWidget.ScrollItem> displayedItems;
    private final int buttonsPerPage = 10;
    private final BlockPos blockPos;
    private PresetOptions presetOptions;
    private String selectedEntity;
    private String textName = "";
    private CachedSkin skin;
    private String prevFilter = "";

    public NewPresetMenu(BlockPos blockPos) {
        super(Text.of("NewPresetMenu"));
        for (String type : NpcsManager.getInstance().getLoadedEntityTypes()) {
            ScrollWidget.ScrollItem scrollItem = new ScrollWidget.ScrollItem(type, TextUtils.idToLegibleText(type), buttonWidget -> {
                selectedEntity = type;
                clear();
            });
            items.add(scrollItem);
        }
        this.displayedItems = items;
        this.blockPos = blockPos;
    }

    @Override
    public void addWidgets() {
        if (client == null) return;
        int startingX = (client.getWindow().getScaledWidth() / 2) - 200;
        int startingY = (client.getWindow().getScaledHeight() / 2) - ((buttonsPerPage + 1) * 10) + 25;
        ScrollWidget scrollWidget = ScrollWidget.builder(this, () -> displayedItems)
                .pos(startingX, startingY)
                .itemsDimensions(150, 20)
                .itemsPerPage(10).build();
        addWidget(scrollWidget);
    }

    @Override
    public void addDrawables() {
        if (client == null) return;
        int startingX = (windowWidth / 2) - 200;
        int startingY = (windowHeight / 2) - ((buttonsPerPage + 1) * 10) - 5;
        DefaultedTextField searchField = new DefaultedTextField(client.textRenderer,
                Text.translatable("screen.cinematiccreeper.search"),
                startingX, startingY,
                150, 20);
        addDrawableChild(searchField);
        searchField.setChangedListener(text -> {
            if (text.equals(prevFilter)) return;
            filterDisplayedItems(text.replace(" ", "_"));
        });
        searchField.setText(prevFilter);
        searchField.setFocused(true);
        if (selectedEntity == null) return;
        addPresetOptions(startingX);
    }

    private void filterDisplayedItems(String filter){
        if (filter.contains(prevFilter)){
            filterAlreadyFiltered(filter);
        } else if (filter.isEmpty()) {
            displayedItems = items;
        } else {
            filterAll(filter);
        }
        prevFilter = filter;
        clear();
    }

    private void filterAlreadyFiltered(String filter){
        displayedItems = displayedItems.stream().filter(item -> item.id().contains(filter)).toList();
    }

    private void filterAll(String filter){
        displayedItems = items.stream().filter(item -> item.id().contains(filter)).toList();
    }

    private void addPresetOptions(int startingX) {
        if (client == null) return;

        final DefaultedTextField nameTextField = new DefaultedTextField(client.textRenderer,
                Text.translatable("screen.cinematiccreeper.preset_id"),
                startingX + 180, client.getWindow().getScaledHeight() / 2 - 85,
                150, 20);
        nameTextField.setChangedListener(text -> textName = text);
        addDrawableChild(nameTextField);
        nameTextField.setText(textName);

        ButtonWidget optionsButton = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.options"), button -> {
            textName = nameTextField.getText();
            client.setScreen(new BasicTypeMenu(selectedEntity, presetOptions1 -> {
                presetOptions = presetOptions1;
                client.setScreen(this);
            }, () -> client.setScreen(this), new PresetOptions().setDisplayName(nameTextField.getText()), null));
        }).dimensions(startingX + 180, client.getWindow().getScaledHeight() / 2 + 75, 150, 20).build();
        addDrawableChild(optionsButton);

        if (selectedEntity.equals(NpcEntity.ENTITY_ID)) {
            ButtonWidget changeSkin = ButtonWidget.builder(Text.of("Skin"), button -> {
                client.setScreen(new SkinClosetMenu(this,
                        (name, skin) -> this.skin = skin,
                        skin == null ? null : skin.getTexture(), DefaultSkinHelper.getTexture()));
            }).dimensions(startingX + 180, client.getWindow().getScaledHeight() / 2 + 55,
                    150, 20).build();
            addDrawableChild(changeSkin);

            ButtonWidget createPreset = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.create"), button -> {
                if (invalidInput(nameTextField)) return;
                String skinUrl = skin == null ? "" : skin.getUrl().toString();
                Preset preset = new Preset(selectedEntity, nameTextField.getText(),
                        presetOptions == null ? new PresetOptions()
                                .setDisplayName(nameTextField.getText())
                                .setSkinUrl(skinUrl) : presetOptions.setSkinUrl(skinUrl));
                PresetsManager.getInstance().addPreset(preset);
                client.setScreen(new PresetsMenu(blockPos));
                NotificationManager.getInstance().add(Notification.CREATED_SUCCESSFULLY);
            }).dimensions(startingX + 180, client.getWindow().getScaledHeight() / 2 + 95, 150, 20).build();
            addDrawableChild(createPreset);
        } else {
            ButtonWidget createPreset = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.create"), button -> {
                if (invalidInput(nameTextField)) return;
                Preset preset = new Preset(selectedEntity, nameTextField.getText(),
                        presetOptions == null ? new PresetOptions().setDisplayName(nameTextField.getText()) : presetOptions);
                PresetsManager.getInstance().addPreset(preset);
                client.setScreen(new PresetsMenu(blockPos));
                NotificationManager.getInstance().add(Notification.CREATED_SUCCESSFULLY);
            }).dimensions(startingX + 180, client.getWindow().getScaledHeight() / 2 + 95, 150, 20).build();
            addDrawableChild(createPreset);
        }
    }

    private boolean invalidInput(TextFieldWidget nameTextField) {
        String input = nameTextField.getText();
        if (input.replace(" ", "").isEmpty()){
            NotificationManager.getInstance().add(Notification.BLANK_ID);
            return true;
        }
        if (PresetsManager.getInstance().getPresetWithId(input).isPresent()) {
            nameTextField.setText("");
            NotificationManager.getInstance().add(Notification.ALREADY_EXISTS);
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (client == null) return;
        int x = client.getWindow().getScaledWidth() / 2 - 150;
        int y = (context.getScaledWindowHeight() / 2) - ((buttonsPerPage + 1) * 10) + 45;
        context.drawCenteredTextWithShadow(client.textRenderer, selectedEntity,
                x + 205,
                y - 45,
                0xffffff);
        renderEntity(context, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    public void renderEntity(DrawContext context, float delta) {
        if (client == null || selectedEntity == null || client.player == null) return;
        Entity entity = NpcsManager.getInstance().getEntityTypeFromId(selectedEntity).create(client.player.clientWorld);
        if (entity == null) return;
        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.translate(client.getWindow().getScaledWidth() / 2f + 55, context.getScaledWindowHeight() / 2f, 50);
        stack.multiply(RotationAxis.NEGATIVE_Z.rotation(MathHelper.PI));
        stack.scale(40, 40, 1);
        stack.translate(0, -dimensions.height / 2, 0);
        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0, 0, 0, 0, delta, stack, context.getVertexConsumers(), 0xF000F0));
        context.draw();
        dispatcher.setRenderShadows(true);
        stack.pop();
    }

    @Override
    public void close() {
        if (client == null) super.close();
        client.setScreen(new PresetsMenu(blockPos));
    }

}



