package me.julionxn.cinematiccreeper.screen.gui.screens.camera;

import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class OptionsCameraMenu extends CameraMenu {

    private double currentSmoothness;

    public OptionsCameraMenu() {
        super();
        currentSmoothness = CameraManager.getInstance().getSmoothness();
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        if (client == null) return;
        TextFieldWidget smoothness = new TextFieldWidget(client.textRenderer, x + 20, y + 20, 100, 20, Text.of("Smoothness"));
        addDrawableChild(smoothness);
        smoothness.setText(String.valueOf(currentSmoothness));
        ButtonWidget saveButton = ButtonWidget.builder(Text.translatable("gui.cinematiccreeper.save"), button -> {
            double value = currentSmoothness;
            try {
                value = Double.parseDouble(smoothness.getText());
            } catch (NumberFormatException e){
                NotificationManager.getInstance().add(Notification.Type.ERROR, Text.translatable("messages.cinematiccreeper.invalid_number"));
            }
            if (value < 0 || value >= 1){
                NotificationManager.getInstance().add(Notification.Type.ERROR, Text.translatable("messages.cinematiccreeper.invalid_number"));
                return;
            }
            CameraManager.getInstance().setSmoothness(value);
            currentSmoothness = value;
        }).dimensions(x + (width / 2) - 50, y + height, 100, 20).build();
        addDrawableChild(saveButton);
    }
}
