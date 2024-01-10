package me.julionxn.cinematiccreeper.screen.gui.screens.camera;

import me.julionxn.cinematiccreeper.core.camera.CameraSettings;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.core.notifications.Notification;
import me.julionxn.cinematiccreeper.core.notifications.NotificationManager;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.TextFieldWithDescription;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.ToggleWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Optional;

public class SettingsCameraMenu extends CameraMenu {

    private double currentSmoothness;
    private double currentMovementSensibility;
    private float currentRotationSmoothness;
    private float currentRotationSensibility;
    private TextFieldWithDescription smoothnessField;
    private TextFieldWithDescription movementSensibilityField;
    private TextFieldWithDescription rotationSmoothnessField;
    private TextFieldWithDescription rotationSensibilityField;

    public SettingsCameraMenu() {
        super();
        CameraSettings settings = CameraManager.getInstance().getSettings();
        currentSmoothness = settings.getSmoothness();
        currentMovementSensibility = settings.getMovementSensibility();
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
        movementSensibilityField = new TextFieldWithDescription(this,
                client.textRenderer, x + 20, y + 40, 140, 100, 20, Text.translatable("camera.cinematiccreeper.movement_sensibility"));
        addWidget(movementSensibilityField);
        rotationSmoothnessField = new TextFieldWithDescription(this,
                client.textRenderer, x + 20, y + 60, 140, 100, 20, Text.translatable("camera.cinematiccreeper.rotation_smoothness"));
        addWidget(rotationSmoothnessField);
        rotationSensibilityField = new TextFieldWithDescription(this,
                client.textRenderer, x + 20, y + 80, 140, 100, 20, Text.translatable("camera.cinematiccreeper.rotation_sensibility"));
        addWidget(rotationSensibilityField);
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        if (client == null) return;
        smoothnessField.getTextFieldWidget().setText(String.valueOf(currentSmoothness));
        movementSensibilityField.getTextFieldWidget().setText(String.valueOf(currentMovementSensibility));
        rotationSmoothnessField.getTextFieldWidget().setText(String.valueOf(currentRotationSmoothness));
        rotationSensibilityField.getTextFieldWidget().setText(String.valueOf(currentRotationSensibility));
        ToggleWidget showOptionsToggle = new ToggleWidget(x + 20, y + 100, 179, 20, Text.translatable("camera.cinematiccreeper.show_controls"),
                () -> CameraManager.getInstance().getSettings().setShowOptions(true),
                () -> CameraManager.getInstance().getSettings().setShowOptions(false));
        showOptionsToggle.setActive(CameraManager.getInstance().getSettings().showOptions());
        addDrawableChild(showOptionsToggle);
        ButtonWidget saveButton = ButtonWidget.builder(Text.translatable("screen.cinematiccreeper.save"), button -> {
            if (smoothnessField == null) return;
            Optional<Double> smoothOpt = validNumber(smoothnessField.getTextFieldWidget().getText(), 0d, 1d);
            Optional<Double> mSensOpt = validNumber(movementSensibilityField.getTextFieldWidget().getText(), -100d, 100d);
            Optional<Float> rSmoothOpt = validNumber(rotationSmoothnessField.getTextFieldWidget().getText(), 0, 1);
            Optional<Float> rSensOpt = validNumber(rotationSensibilityField.getTextFieldWidget().getText(), 0, 20);
            if (smoothOpt.isEmpty() || mSensOpt.isEmpty() || rSmoothOpt.isEmpty() || rSensOpt.isEmpty()){
                NotificationManager.getInstance().add(Notification.INVALID_NUMBER);
                return;
            }
            CameraSettings settings = CameraManager.getInstance().getSettings();
            settings.setSmoothness(smoothOpt.get());
            settings.setMovementSensibility(mSensOpt.get());
            settings.setRotationSmoothness(rSmoothOpt.get());
            settings.setRotationSensibility(rSensOpt.get());
            currentSmoothness = smoothOpt.get();
            currentMovementSensibility = mSensOpt.get();
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
