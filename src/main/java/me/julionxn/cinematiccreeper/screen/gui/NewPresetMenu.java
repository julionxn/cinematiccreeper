package me.julionxn.cinematiccreeper.screen.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.julionxn.cinematiccreeper.presets.Preset;
import me.julionxn.cinematiccreeper.presets.PresetsManager;
import me.julionxn.cinematiccreeper.screen.gui.widgets.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.widgets.ScrollItem;
import me.julionxn.cinematiccreeper.screen.gui.widgets.ScrollWidget;
import me.julionxn.cinematiccreeper.util.TextUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;

import java.util.ArrayList;
import java.util.List;

public class NewPresetMenu extends ExtendedScreen {

    private final List<String> types;
    private final List<ScrollItem> items = new ArrayList<>();
    private final int buttonsPerPage = 10;
    private String selectedEntity;

    public NewPresetMenu() {
        super(Text.of("NewPresetMenu"));
        types = PresetsManager.getInstance().getLoadedEntityTypes();
        for (String type : types) {
            ScrollItem scrollItem = new ScrollItem(TextUtils.idToLegibleText(type), buttonWidget -> {
                selectedEntity = type;
                clearChildrenS();
            });
            items.add(scrollItem);
        }
    }

    @Override
    public void addWidgets() {
        if (client == null) return;
        int startingX = (client.getWindow().getScaledWidth() / 2) - 200;
        int startingY = (client.getWindow().getScaledHeight() / 2) - ((buttonsPerPage + 1) * 10) + 25;
        ScrollWidget scrollWidget = new ScrollWidget(this, startingX, startingY, 150, 20, 10, items);
        addWidget(scrollWidget);
    }

    @Override
    public void addDrawables() {
        if (client == null) return;
        int startingX = (windowWidth / 2) - 200;
        int startingY = (windowHeight / 2) - ((buttonsPerPage + 1) * 10) - 5;
        TextFieldWidget searchField = new TextFieldWidget(client.textRenderer, startingX, startingY, 150, 20, Text.of("Buscar"));
        addDrawableChild(searchField);
        ButtonWidget searchButton = ButtonWidget.builder(Text.of("S"), button -> {
            String searchText = searchField.getText();
            if (types.contains(searchText)) {
                selectedEntity = searchText;
            }
        }).dimensions(startingX + 150, startingY, 20, 20).build();
        addDrawableChild(searchButton);
        if (selectedEntity == null) return;
        TextFieldWidget nameTextField = new TextFieldWidget(client.textRenderer,
                startingX + 180, client.getWindow().getScaledHeight() / 2 - 85, 150, 20, Text.of("Nombre"));
        addDrawableChild(nameTextField);
        TextFieldWidget skinBase64Field;
        if (selectedEntity.equals("cinematiccreeper:npc_entity")) {
            skinBase64Field = new TextFieldWidget(client.textRenderer,
                    startingX + 180, client.getWindow().getScaledHeight() / 2 + 55, 150, 20, Text.of("Skin"));
            addDrawableChild(skinBase64Field);
        } else {
            skinBase64Field = null;
        }
        ButtonWidget createPreset = ButtonWidget.builder(Text.of("Crear"), button -> {
            Preset preset;
            if (selectedEntity.equals("cinematiccreeper:npc_entity")) {
                preset = new Preset(selectedEntity, nameTextField.getText(), skinBase64Field.getText());
            } else {
                preset = new Preset(selectedEntity, nameTextField.getText());
            }
            PresetsManager.getInstance().addPreset(preset);
            client.setScreen(new NpcsMenu());
        }).dimensions(startingX + 180, client.getWindow().getScaledHeight() / 2 + 95, 150, 20).build();
        addDrawableChild(createPreset);
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
        Entity entity = PresetsManager.getInstance().getEntityTypeFromId(selectedEntity).create(client.player.clientWorld);
        if (entity == null) return;
        EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.translate(client.getWindow().getScaledWidth() / 2f + 55, context.getScaledWindowHeight() / 2f, 50);
        stack.multiply(RotationAxis.NEGATIVE_Z.rotation((float) Math.PI));
        stack.scale(40, 40, 1);
        stack.translate(0, -dimensions.height / 2, 0);
        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() -> {
            dispatcher.render(entity, 0, 0, 0, 0, delta, stack, context.getVertexConsumers(), 0xF000F0);
        });
        context.draw();
        dispatcher.setRenderShadows(true);
        stack.pop();
    }
}



