package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options.poses;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.managers.NpcPosesManager;
import me.julionxn.cinematiccreeper.poses.Easing;
import me.julionxn.cinematiccreeper.poses.NpcPose;
import me.julionxn.cinematiccreeper.poses.PosePoint;
import me.julionxn.cinematiccreeper.poses.PoseType;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.PosePointWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DynamicPoseMenu extends ExtendedScreen {

    private static final Identifier POINT_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "point.png");
    private static final Identifier TIMELINE_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/timeline.png");
    private final Screen previousScreen;
    private final String id;
    private int currentTick = 0;
    private int displayLength = 60;
    private int previousMaxTickPoint = 0;
    private int currentLength = 0;
    private final NpcPose npcPose = new NpcPose(PoseType.DYNAMIC);

    public DynamicPoseMenu(Screen previousScreen, String id) {
        super(Text.of("StaticPose"));
        this.previousScreen = previousScreen;
        this.id = id;
    }

    @Override
    public void addWidgets() {
        if (client == null) return;
        int windowWidth = client.getWindow().getScaledWidth();
        int windowHeight = client.getWindow().getScaledHeight();
        if (npcPose.containsAPose(currentTick)){
            PosePointWidget widget = new PosePointWidget(this, windowWidth / 2 - 30, windowHeight / 2 - 60, npcPose.getPoseOfTick(currentTick));
            addWidget(widget);
        }
    }

    @Override
    public void addDrawables() {
        if (client == null) return;
        addChangeTickButton("<", windowWidth / 2 - 170, -1);
        addChangeTickButton("<<", windowWidth / 2 - 190, -20);
        addChangeTickButton(">", windowWidth / 2 + 150, 1);
        addChangeTickButton(">>", windowWidth / 2 + 170, 20);
        ButtonWidget addPose = ButtonWidget.builder(Text.of("Añadir punto"), button -> addPoint(new PosePoint()))
                .dimensions(20, windowHeight - 30, 100, 20).build();
        addDrawableChild(addPose);
        if (npcPose.containsAPose(currentTick)){
            addEaseTypeButton(Easing.NONE, windowWidth / 2 - 190, windowHeight / 2 - 30);
            addEaseTypeButton(Easing.EASE_IN, windowWidth / 2 - 190, windowHeight / 2 - 10);
            addEaseTypeButton(Easing.EASE_OUT, windowWidth / 2 - 190, windowHeight / 2 + 10);
            ButtonWidget removePose = ButtonWidget.builder(Text.of("Quitar punto"), button -> removePoint(currentTick))
                    .dimensions(120, windowHeight - 30, 100, 20).build();
            addDrawableChild(removePose);
        }
        ButtonWidget createButton = ButtonWidget.builder(Text.of("Hecho"), button -> {
            NpcPosesManager.getInstance().addNpcPose(id, npcPose);
            client.setScreen(previousScreen);
        }).dimensions(windowWidth - 120, windowHeight - 30, 100, 20).build();
        addDrawableChild(createButton);
        ButtonWidget playButton = ButtonWidget.builder(Text.of("Play"), button -> {
            System.out.println("PLAY");
        }).dimensions(windowWidth - 120, windowHeight - 50, 100, 20).build();
        addDrawableChild(playButton);
    }

    private void addEaseTypeButton(Easing easing, int x, int y){
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of(easing.toString().replace("_", " ")), button -> {
            npcPose.getPoseOfTick(currentTick).easing = easing;
        }).dimensions(x, y, 80, 20).build();
        addDrawableChild(buttonWidget);
    }

    private void addChangeTickButton(String text, int x, int in){
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of(text), button -> adjustTick(in))
                .dimensions(x, 20, 20, 20)
                .build();
        addDrawableChild(buttonWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (client == null) return;
        int centerX = windowWidth  / 2;
        context.drawCenteredTextWithShadow(client.textRenderer, "Tick: " + currentTick + " / " + displayLength,
                centerX, 8, 0xffffff);
        context.drawTexture(TIMELINE_TEXTURE, centerX - 150, 20, 10, 0, 0, 300, 20, 300, 20);
        super.render(context, mouseX, mouseY, delta);
        int width = 296;
        int startingX = centerX - 148;
        float spacing = (float) width / displayLength;
        float remainingSpacing = width - (spacing * displayLength);
        for (int i = 0; i < displayLength + 1; i++) {
            boolean isSecond = i % 20 == 0;
            int x = (int) (startingX + spacing * i);
            if (i != 0 && remainingSpacing > 0) {
                x += 1;
                remainingSpacing -= 1;
            }
            int y = isSecond ? 22 : 26;
            int height = isSecond ? 16 : 7;
            int color = i == currentTick ? 0xff3af6eb : npcPose.containsAPose(i) ? 0xffff1717 : 0xff8d697a;
            context.fill(x, y, x + 1, y + height, 100, color);
        }
        if (npcPose.containsAPose(currentTick)){
            Easing easing = npcPose.getPoseOfTick(currentTick).easing;
            int y = easing == Easing.NONE ? 0 : easing == Easing.EASE_IN ? 20 : 40;
            context.drawTexture(POINT_TEXTURE, windowWidth / 2 - 210, windowHeight / 2 - 30 + y, 0, 0, 0, 20, 20, 20, 20);
        }
    }

    private void adjustTick(int in){
        if (currentTick + in < 0) {
            setCurrentTick(0);
            return;
        }
        setCurrentTick(currentTick + in);
    }

    private void setCurrentTick(int tick){
        if (tick < 0) return;
        displayLength = Math.max(tick, Math.max(60, currentLength));
        currentTick = tick;
        clear();
    }

    private void addPoint(PosePoint posePoint){
        npcPose.setPose(currentTick, posePoint);
        if (currentTick > currentLength){
            previousMaxTickPoint = currentLength;
            currentLength = currentTick;
        }
        clear();
    }

    private void removePoint(int tick){
        npcPose.removePose(tick);
        if (tick == currentLength){
            currentLength = previousMaxTickPoint;
            if (currentLength > 60){
                setCurrentTick(currentLength);
            }
        }
        clear();
    }
}
