package me.julionxn.cinematiccreeper.screen.gui.screens.skins;

import me.julionxn.cinematiccreeper.core.managers.NpcSkinManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.core.skins.CachedSkin;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.RemovableItemsScrollWidget;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.SkinClosetWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SkinClosetMenu extends ExtendedScreen {

    private final List<RemovableItemsScrollWidget.RemovableScrollItem> scrollItems = new ArrayList<>();
    SkinClosetWidget widget;
    private final Screen previousScreen;
    private final BiConsumer<String, CachedSkin> onChanged;
    private final Identifier skin;
    private final Identifier noneSkin;
    private @Nullable String selectedName;
    private @Nullable CachedSkin selectedSkin;

    public SkinClosetMenu(Screen previousScreen, BiConsumer<String, CachedSkin> onChanged, @Nullable Identifier skin, @Nullable Identifier noneSkin) {
        super(Text.of("SkinClosetMenu"));
        this.previousScreen = previousScreen;
        this.onChanged = onChanged;
        this.skin = skin;
        this.noneSkin = noneSkin;
        setItems();
    }

    @Override
    public void addWidgets() {
        if (client == null) return;
        int x = windowWidth / 2 - 140;
        int y = windowHeight / 2 - 80;
        widget = new SkinClosetWidget(this, x, y,
                () -> {
                    setItems();
                    return scrollItems;
                }, skin);
        addWidget(widget);
    }

    @Override
    public void addDrawables() {
        ButtonWidget returnButton = ButtonWidget.builder(Text.of("<-"), button -> close())
                .dimensions(10, 10, 20, 20).build();
        addDrawableChild(returnButton);
        int x = windowWidth / 2 - 120;
        int y = windowHeight / 2 + 80;
        ButtonWidget saveButton = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.save"), button -> {
            onChanged.accept(selectedName, selectedSkin);
        }).dimensions(x, y, 120, 20).build();
        addDrawableChild(saveButton);
    }

    public void setItems(){
        scrollItems.clear();
        List<String> options = new ArrayList<>();
        String noneText = Text.translatable("screen.cinematiccreeper.none").getString();
        options.add(noneText);
        List<String> cachedSkins = NpcSkinManager.getInstance().getSavedSkins().stream().toList();
        options.addAll(cachedSkins);
        for (String name : options){
            if (name.equals(noneText)){
                scrollItems.add(new RemovableItemsScrollWidget.RemovableScrollItem(name, buttonWidget -> {
                    this.selectedName = null;
                    this.selectedSkin = null;
                    widget.changeSkin(noneSkin);
                }, buttonWidget -> {}));
                continue;
            }
            scrollItems.add(new RemovableItemsScrollWidget.RemovableScrollItem(name, buttonWidget -> {
                if (widget == null) return;
                CachedSkin cachedSkin = NpcSkinManager.getInstance().getSkinFromName(name);
                Identifier texture = cachedSkin.getTexture();
                if (texture == null) {
                    NotificationManager.getInstance().add(Notification.Type.ERROR, Text.of("Still loading."));
                    return;
                }
                this.selectedName = name;
                this.selectedSkin = cachedSkin;
                widget.changeSkin(texture);
            }, buttonWidget -> {
                NpcSkinManager.getInstance().removeSavedSkin(name);
                setItems();
            }));
        }
    }

    @Override
    public void close() {
        super.close();
        if (previousScreen != null) MinecraftClient.getInstance().setScreen(previousScreen);
    }
}
