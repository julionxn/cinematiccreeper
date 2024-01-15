package me.julionxn.cinematiccreeper.util.colors;

import java.util.HashMap;

public class Color {

    private final HashMap<Component, Float> components = new HashMap<>();
    private int color;

    public Color(int color){
        setColor(color);
    }

    public int getColor(){
        return color;
    }

    public int getColorWithAlpha(){
        return (255 << 24) | (color & 0x00ffffff);
    }

    public void setColor(int color){
        this.color = color;
        components.put(Component.RED, getComponent(Component.RED));
        components.put(Component.GREEN, getComponent(Component.GREEN));
        components.put(Component.BLUE, getComponent(Component.BLUE));
    }

    public void setRed(int red){
        components.put(Component.RED, (float) red / 255f);
        updateColor();
    }

    public void setGreen(int green){
        components.put(Component.GREEN, (float) green / 255f);
        updateColor();
    }

    public void setBlue(int blue){
        components.put(Component.BLUE, (float) blue / 255f);
        updateColor();
    }

    public float getRed(){
        return components.get(Component.RED);
    }

    public float getGreen(){
        return components.get(Component.GREEN);
    }

    public float getBlue(){
        return components.get(Component.BLUE);
    }

    public float getComponent(Component component) {
        return switch (component) {
            case RED -> ((color >> 16) & 0xFF) / 255.0f;
            case GREEN -> ((color >> 8) & 0xFF) / 255.0f;
            default -> (color & 0xFF) / 255.0f;
        };
    }

    private void updateColor(){
        int r = toInt(getRed());
        int g = toInt(getGreen());
        int b = toInt(getBlue());
        color = (r << 16) | (g << 8) | b;
    }

    public int toInt(float value){
        return (int) (value * 255f);
    }

    public enum Component{
        RED, GREEN, BLUE
    }

}
