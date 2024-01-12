package me.julionxn.cinematiccreeper.core.camera;

import com.google.gson.annotations.Expose;

public class CameraSettings {

    @Expose private double smoothness = 0.95;
    @Expose private double movementSensibility = 1;
    @Expose private float rotationSmoothness = 0.95f;
    @Expose private float rotationSensibility = 0.1f;
    @Expose private boolean showOptions = true;
    @Expose private boolean showGrid = false;

    public double getSmoothness() {
        return smoothness;
    }

    public void setSmoothness(double smoothness) {
        this.smoothness = smoothness;
    }

    public float getRotationSmoothness() {
        return rotationSmoothness;
    }

    public void setRotationSmoothness(float rotationSmoothness) {
        this.rotationSmoothness = rotationSmoothness;
    }

    public float getRotationSensibility() {
        return rotationSensibility;
    }

    public void setRotationSensibility(float rotationSensibility) {
        this.rotationSensibility = rotationSensibility;
    }

    public double getMovementSensibility() {
        return movementSensibility;
    }

    public void setMovementSensibility(double movementSensibility) {
        this.movementSensibility = movementSensibility;
    }

    public boolean showOptions() {
        return showOptions;
    }

    public void setShowOptions(boolean showOptions) {
        this.showOptions = showOptions;
    }

    public boolean showGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }
}
