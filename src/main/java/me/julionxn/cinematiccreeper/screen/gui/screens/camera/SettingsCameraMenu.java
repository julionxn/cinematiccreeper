package me.julionxn.cinematiccreeper.screen.gui.screens.camera;

import me.julionxn.cinematiccreeper.core.camera.CameraSettings;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.TextFieldWithDescription;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Optional;

public class SettingsCameraMenu extends CameraMenu {

    private double currentSmoothness;
    private float currentRotationSmoothness;
    private float currentRotationSensibility;
    private TextFieldWithDescription smoothnessField;
    private TextFieldWithDescription rotationSmoothnessField;
    private TextFieldWithDescription rotationSensibilityField;

    public SettingsCameraMenu() {
        super();
        CameraSettings settings = CameraManager.getInstance().getSettings();
        currentSmoothness = settings.getSmoothness();
        currentRotationSmoothness = settings.getRotationSmoothness();
        currentRotationSensibility = settings.getRotationSensibility();
    }

    @Override
    public void addWidgets() {
        super.addWidgets();
        if (client == null) return;
        smoothnessField = new TextFieldWithDescription(this,
                client.textRenderer, x + 20, y + 20, 140, 100, 20, Text.translatable("camera.cinematiccreeper.smoothness"));
        addWidget(smoothnessField);
        rotationSmoothnessField = new TextFieldWithDescription(this,
                client.textRenderer, x + 20, y + 40, 140, 100, 20, Text.translatable("camera.cinematiccreeper.rotation_smoothness"));
        addWidget(rotationSmoothnessField);
        rotationSensibilityField = new TextFieldWithDescription(this,
                client.textRenderer, x + 20, y + 60, 140, 100, 20, Text.translatable("camera.cinematiccreeper.rotation_sensibility"));
        addWidget(rotationSensibilityField);
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        if (client == null) return;
        smoothnessField.getTextFieldWidget().setText(String.valueOf(currentSmoothness));
        rotationSmoothnessField.getTextFieldWidget().setText(String.valueOf(currentRotationSmoothness));
        rotationSensibilityField.getTextFieldWidget().setText(String.valueOf(currentRotationSensibility));
        ButtonWidget saveButton = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.save"), button -> {
            if (smoothnessField == null) return;
            Optional<Double> smoothOpt = validNumber(smoothnessField.getTextFieldWidget().getText(), 0d, 1d);
            Optional<Float> rSmoothOpt = validNumber(rotationSmoothnessField.getTextFieldWidget().getText(), 0, 1);
            Optional<Float> rSensOpt = validNumber(rotationSensibilityField.getTextFieldWidget().getText(), 0, 20);
            if (smoothOpt.isEmpty() || rSmoothOpt.isEmpty() || rSensOpt.isEmpty()){
                NotificationManager.getInstance().add(Notification.INVALID_NUMBER);
                return;
            }
            CameraSettings settings = CameraManager.getInstance().getSettings();
            settings.setSmoothness(smoothOpt.get());
            settings.setRotationSmoothness(rSmoothOpt.get());
            settings.setRotationSensibility(rSensOpt.get());
            currentSmoothness = smoothOpt.get();
            currentRotationSmoothness = rSmoothOpt.get();
            currentRotationSensibility = rSmoothOpt.get();
            NotificationManager.getInstance().add(Notification.SAVED);
        }).dimensions(x + (width / 2) - 50, y + height, 100, 20).build();
        addDrawableChild(saveButton);
    }

    private Optional<Float> validNumber(String number, float min, float max){
        float value;
        try {
            value = Float.parseFloat(number);
        } catch (NumberFormatException e){
            return Optional.empty();
        }
        if (value >= min && value < max){
            return Optional.of(value);
        }
        return Optional.empty();
    }

    private Optional<Double> validNumber(String number, double min, double max){
        double value;
        try {
            value = Double.parseDouble(number);
        } catch (NumberFormatException e){
            return Optional.empty();
        }
        if (value >= min && value < max){
            return Optional.of(value);
        }
        return Optional.empty();
    }
}
