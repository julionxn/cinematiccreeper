package me.julionxn.cinematiccreeper.screen.gui.screens.camera;

import me.julionxn.cinematiccreeper.core.camera.CameraRecording;
import me.julionxn.cinematiccreeper.core.managers.CameraManager;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.RemovableItemsScrollWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class RecordingsCameraMenu extends CameraMenu {

    private final List<RemovableItemsScrollWidget.RemovableScrollItem> scrollItems = new ArrayList<>();

    protected RecordingsCameraMenu() {
        super();
        setItems();
    }

    private void setItems() {
        scrollItems.clear();
        CameraManager manager = CameraManager.getInstance();
        List<CameraRecording> recordings = manager.getCameraRecordings();
        for (CameraRecording recording : recordings) {
            scrollItems.add(new RemovableItemsScrollWidget.RemovableScrollItem(recording.id, buttonWidget -> {
                CameraManager.getInstance().playRecording(recording);
            }, (buttonWidget) -> manager.removeCameraRecording(recording)));
        }
    }

    @Override
    public void addWidgets() {
        super.addWidgets();
        RemovableItemsScrollWidget scrollWidget = RemovableItemsScrollWidget.builder(this, () -> {
                    setItems();
                    return scrollItems;
                })
                .pos(x + 80, y + 40)
                .itemsDimensions(100, 20)
                .itemsPerPage(7).build();
        addWidget(scrollWidget);
    }

    @Override
    public void addDrawables() {
        super.addDrawables();
        ButtonWidget addNewButton = ButtonWidget.builder(Text.of("+"), button -> {
            if (client == null) return;
            client.setScreen(new NewCameraRecordingMenu());
        }).dimensions(x + 140, y + 20, 20, 20).build();
        addDrawableChild(addNewButton);
    }

}
