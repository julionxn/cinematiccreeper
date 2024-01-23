package me.julionxn.cinematiccreeper.screen.gui.screens.skins;

import me.julionxn.cinematiccreeper.core.managers.NpcSkinManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.DefaultedTextField;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;

import java.net.MalformedURLException;

public class AddSavedSkinMenu extends Screen {

    private final SkinClosetMenu prevScreen;

    public AddSavedSkinMenu(SkinClosetMenu prevScreen) {
        super(Text.of("AddSavedSkin"));
        this.prevScreen = prevScreen;
    }

    @Override
    protected void init() {
        if (client == null) return;
        Window window = client.getWindow();
        int x = window.getScaledWidth() / 2;
        int y = window.getScaledHeight() / 2;
        DefaultedTextField nameField = new DefaultedTextField(client.textRenderer,
                Text.translatable("screen.cinematiccreeper.name"),
                x - 60, y - 30, 120, 20);
        addDrawableChild(nameField);
        DefaultedTextField urlField = new DefaultedTextField(client.textRenderer,
                Text.translatable("screen.cinematiccreeper.skin_url"),
                x - 60, y - 10, 120, 20);
        addDrawableChild(urlField);
        ButtonWidget saveButton = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.save"), button -> {
            String name = nameField.getText();
            String url = urlField.getText();
            if (name.replace(" ", "").isEmpty() ||
                url.replace(" ", "").isEmpty()){
                NotificationManager.getInstance().add(Notification.BLANK_FIELD);
                return;
            }
            try {
                NpcSkinManager.getInstance().addSavedSkin(name, url, () -> {
                    NotificationManager.getInstance().add(Notification.SAVED);
                    MinecraftClient.getInstance().setScreen(prevScreen);
                });
            } catch (MalformedURLException e) {
                NotificationManager.getInstance().add(new Notification(Notification.Type.ERROR,
                        Text.translatable("messages.cinematiccreeper.invalid_url")));
            }
        }).dimensions(x - 60, y + 10, 120, 20).build();
        addDrawableChild(saveButton);
    }

    @Override
    public void close() {
        if (client == null) return;
        client.setScreen(prevScreen);
    }
}
