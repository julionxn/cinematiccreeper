package me.julionxn.cinematiccreeper.screen.gui.screens.npc_options.poses;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.managers.NpcPosesManager;
import me.julionxn.cinematiccreeper.poses.*;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.widgets.PosePointWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Vector3f;

public class DynamicPoseMenu extends ExtendedScreen {

    private static final Identifier POINT_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "point.png");
    private static final Identifier TIMELINE_TEXTURE = new Identifier(CinematicCreeper.MOD_ID, "textures/gui/timeline.png");
    private final Screen previousScreen;
    private final String id;
    private int prevToPlayTick = 0;
    private int currentTick = 0;
    private int displayLength = 60;
    private int previousMaxTickPoint = 0;
    private int currentLength = 0;
    private final NpcPose npcPose = new NpcPose(PoseType.DYNAMIC);
    private PoseTicker ticker;
    private boolean playing = false;
    private PlayerEntityModel<NpcEntity> model;

    public DynamicPoseMenu(Screen previousScreen, String id) {
        super(Text.of("StaticPose"));
        this.previousScreen = previousScreen;
        this.id = id;
    }

    @Override
    protected void init() {
        super.init();
        if (client != null && model == null){
            EntityModelLoader loader = client.getEntityModelLoader();
            PlayerEntityModel<NpcEntity> model = new PlayerEntityModel<>(loader.getModelPart(EntityModelLayers.PLAYER), false);
            model.head.scale(new Vector3f(-0.33f));
            ticker = new PoseTicker(model);
        }
    }

    @Override
    public void addWidgets() {
        if (client == null) return;
        int windowWidth = client.getWindow().getScaledWidth();
        int windowHeight = client.getWindow().getScaledHeight();
        if (npcPose.containsAPose(currentTick) && !playing){
            PosePointWidget widget = new PosePointWidget(this, windowWidth / 2 - 30, windowHeight / 2 - 60, npcPose.getPoseOfTick(currentTick));
            addWidget(widget);
        }
    }

    @Override
    public void addDrawables() {
        if (client == null) return;
        String text = playing ? "Pausar" : "Play";
        ButtonWidget playButton = ButtonWidget.builder(Text.of(text), button -> {
            if (playing){
                playing = false;
                ticker.stop();
                currentTick = prevToPlayTick;
            } else {
                playing = true;
                prevToPlayTick = currentTick;
                ticker.play();
            }
            clear();
        }).dimensions(windowWidth - 120, windowHeight - 50, 100, 20).build();
        addDrawableChild(playButton);
        if (playing) return;
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
    public void tick() {
        super.tick();
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
        if (playing && ticker != null){
            ticker.delta(npcPose, delta);
            MatrixStack stack = context.getMatrices();
            stack.push();
            stack.translate(centerX, windowHeight / 2f - 120, 200);
            stack.multiply(RotationAxis.NEGATIVE_Y.rotation((float) Math.PI));
            stack.scale(120, 120, 120);
            ticker.render(stack, context.getVertexConsumers().getBuffer(RenderLayer.getEntityAlpha(DefaultSkinHelper.getTexture())), 0xffffff);
            stack.pop();
            currentTick = ticker.getCurrentTick();
        } else {
            if (npcPose.containsAPose(currentTick)){
                Easing easing = npcPose.getPoseOfTick(currentTick).easing;
                int y = easing == Easing.NONE ? 0 : easing == Easing.EASE_IN ? 20 : 40;
                context.drawTexture(POINT_TEXTURE, windowWidth / 2 - 210, windowHeight / 2 - 30 + y, 0, 0, 0, 20, 20, 20, 20);
            }
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
