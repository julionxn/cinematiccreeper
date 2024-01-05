package me.julionxn.cinematiccreeper.screen.gui.screens.poses;

import me.julionxn.cinematiccreeper.core.managers.NpcPosesManager;
import me.julionxn.cinematiccreeper.core.poses.NpcPose;
import me.julionxn.cinematiccreeper.core.poses.PosePoint;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.PosePointWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class StaticPoseMenu extends ExtendedScreen {

    private final Screen previousScreen;
    private final String id;
    private final NpcPose npcPose = new NpcPose();
    private final PosePoint posePoint = new PosePoint();

    public StaticPoseMenu(Screen previousScreen, String id) {
        super(Text.of("StaticPose"));
        this.previousScreen = previousScreen;
        this.id = id;
    }

    @Override
    public void addWidgets() {
        if (client == null) return;
        int windowWidth = client.getWindow().getScaledWidth();
        int windowHeight = client.getWindow().getScaledHeight();
        PosePointWidget widget = new PosePointWidget(this, windowWidth / 2 - 70, windowHeight / 2 - 80, posePoint);
        addWidget(widget);
    }

    @Override
    public void addDrawables() {
        if (client == null) return;
        int windowWidth = client.getWindow().getScaledWidth();
        int windowHeight = client.getWindow().getScaledHeight();
        ButtonWidget createButton = ButtonWidget.builder(Text.of("Hecho"), button -> {
            npcPose.setPose(posePoint);
            NpcPosesManager.getInstance().addNpcPose(id, npcPose);
            client.setScreen(previousScreen);
        }).dimensions(windowWidth - 120, windowHeight - 40, 100, 20).build();
        addDrawableChild(createButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}
