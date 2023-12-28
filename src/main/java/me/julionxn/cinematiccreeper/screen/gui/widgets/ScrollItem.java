package me.julionxn.cinematiccreeper.screen.gui.widgets;

import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.function.Consumer;

public record ScrollItem(String text, Consumer<ButtonWidget> runnable) {
}
