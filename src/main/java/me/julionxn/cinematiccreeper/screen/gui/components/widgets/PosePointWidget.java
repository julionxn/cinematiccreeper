package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.core.poses.PoseData;
import me.julionxn.cinematiccreeper.core.poses.PosePoint;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedWidget;
import me.julionxn.cinematiccreeper.util.MathHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

import java.util.List;

public class PosePointWidget extends ExtendedWidget {

    private static final Identifier BACKGROUND = texture("pose_widget_bg.png");
    private static final Identifier TRANSLATION_ICON = texture("translation.png");
    private static final Identifier ROTATION_ICON = texture("rotation.png");
    private static final Identifier SCALE_ICON = texture("scale.png");
    private static final Identifier PART_HEAD_ICON = texture("part_head.png");
    private static final Identifier PART_TORSO_ICON = texture("part_torso.png");
    private static final Identifier PART_LEFT_ARM_ICON = texture("part_left_arm.png");
    private static final Identifier PART_RIGHT_ARM_ICON = texture("part_right_arm.png");
    private static final Identifier PART_LEFT_LEG_ICON = texture("part_left_leg.png");
    private static final Identifier PART_RIGHT_LEG_ICON = texture("part_right_leg.png");
    private final int x;
    private final int y;
    private final PosePoint posePoint;
    private Type type = Type.ROTATION;
    private Part part = Part.HEAD;
    private PlayerEntityModel<NpcEntity> model;
    private float modelYaw = 0;
    private float modelPitch = 0;
    private final ImmutableMap<Type, List<String>> texts = ImmutableMap.of(
            Type.ROTATION, List.of("Yaw", "Pitch", "Roll"),
            Type.TRANSLATION, List.of("X", "Y", "Z"),
            Type.SCALE, List.of("Width", "Height", "Deep")
    );
    private final ImmutableMap<Type, List<Float>> ranges = ImmutableMap.of(
            Type.ROTATION, List.of(-MathHelper.PI, MathHelper.PI),
            Type.TRANSLATION, List.of(-20f, 20f),
            Type.SCALE, List.of(-3f, 3f)
    );

    public PosePointWidget(ExtendedScreen screen, int x, int y, PosePoint posePoint) {
        super(screen);
        this.x = x;
        this.y = y;
        this.posePoint = posePoint;
    }

    @Override
    public void init() {
        if (client != null && model == null) {
            EntityModelLoader loader = client.getEntityModelLoader();
            model = new PlayerEntityModel<>(loader.getModelPart(EntityModelLayers.PLAYER), false);
        }
        typeButtonOf(ROTATION_ICON, x, y + 42, Type.ROTATION);
        typeButtonOf(TRANSLATION_ICON, x, y + 72, Type.TRANSLATION);
        typeButtonOf(SCALE_ICON, x, y + 102, Type.SCALE);

        int currentX = x + 36;

        partButtonOf(PART_TORSO_ICON, currentX, y + 15, Part.TORSO);
        partButtonOf(PART_HEAD_ICON,  currentX + 30, y + 15, Part.HEAD);
        partButtonOf(PART_RIGHT_ARM_ICON, currentX + 60, y + 15, Part.RIGHT_ARM);
        partButtonOf(PART_LEFT_ARM_ICON, currentX + 90, y + 15, Part.LEFT_ARM);
        partButtonOf(PART_RIGHT_LEG_ICON, currentX + 120, y + 15, Part.RIGHT_LEG);
        partButtonOf(PART_LEFT_LEG_ICON, currentX + 150, y + 15, Part.LEFT_LEG);
        SliderWidget<Float> yawWidget = SliderWidget.builder(Float.class,
                () -> switch (type){
                    case ROTATION -> getDataFromPart(part).yaw;
                    case TRANSLATION -> getDataFromPart(part).translationX;
                    case SCALE -> getDataFromPart(part).scaleX;
                },
                newValue -> {
                    PoseData data = getDataFromPart(part);
                    switch (type){
                        case ROTATION -> data.yaw = newValue;
                        case TRANSLATION -> data.translationX = newValue;
                        case SCALE -> data.scaleX = newValue;
                    }
                }).pos(currentX + 15, y + 45)
                .range(() -> ranges.get(type).get(0), () -> ranges.get(type).get(1)).wrap()
                .message(() -> Text.of(texts.get(type).get(0)))
                .overlayText(value -> {
                    if (type == Type.ROTATION){
                        return String.valueOf(value * 57.29577951308232f);
                    }
                    return String.valueOf(value);
                }).build();
        addDrawableChild(yawWidget);
        SliderWidget<Float> pitchWidget = SliderWidget.builder(Float.class,
                () -> switch (type){
                    case ROTATION -> getDataFromPart(part).pitch;
                    case TRANSLATION -> getDataFromPart(part).translationY;
                    case SCALE -> getDataFromPart(part).scaleY;
                },
                newValue -> {
                    PoseData data = getDataFromPart(part);
                    switch (type){
                        case ROTATION -> data.pitch = newValue;
                        case TRANSLATION -> data.translationY = newValue;
                        case SCALE -> data.scaleY = newValue;
                    }
                }).pos(currentX + 15, y + 80)
                .range(() -> ranges.get(type).get(0), () -> ranges.get(type).get(1)).wrap()
                .message(() -> Text.of(texts.get(type).get(1)))
                .overlayText(value -> {
                    if (type == Type.ROTATION){
                        return String.valueOf(value * 57.29577951308232f);
                    }
                    return String.valueOf(value);
                }).build();
        addDrawableChild(pitchWidget);
        SliderWidget<Float> rollWidget = SliderWidget.builder(Float.class,
                () -> switch (type){
                    case ROTATION -> getDataFromPart(part).roll;
                    case TRANSLATION -> getDataFromPart(part).translationZ;
                    case SCALE -> getDataFromPart(part).scaleZ;
                },
                newValue -> {
                    PoseData data = getDataFromPart(part);
                    switch (type){
                        case ROTATION -> data.roll = newValue;
                        case TRANSLATION -> data.translationZ = newValue;
                        case SCALE -> data.scaleZ = newValue;
                    }
                }).pos(currentX + 15, y + 115)
                .range(() -> ranges.get(type).get(0), () -> ranges.get(type).get(1)).wrap()
                .message(() -> Text.of(texts.get(type).get(2)))
                .overlayText(value -> {
                    if (type == Type.ROTATION){
                        return String.valueOf(value * 57.29577951308232f);
                    }
                    return String.valueOf(value);
                }).build();
        addDrawableChild(rollWidget);
    }

    private void typeButtonOf(Identifier texture, int x, int y, Type type) {
        TexturedButtonWidget buttonWidget = new TexturedButtonWidget(texture, x, y, button -> this.type = type);
        addDrawableChild(buttonWidget);
    }

    private void partButtonOf(Identifier texture, int x, int y, Part part) {
        TexturedButtonWidget buttonWidget = new TexturedButtonWidget(texture, x, y, button -> this.part = part);
        addDrawableChild(buttonWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (client == null) return;
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.translate(x + 340, y + 50, 200);
        stack.multiply(RotationAxis.NEGATIVE_X.rotation(MathHelper.PI));
        stack.translate(0, -35, 0);
        stack.multiply(new Quaternionf().rotationZYX(0, modelYaw + MathHelper.PI, modelPitch));
        stack.translate(0, 35, 0);
        stack.scale(-70, -70, -70);
        applyPosePointToRenderer(model);
        RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(DefaultSkinHelper.getTexture());
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(renderLayer);
        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() -> renderModel(stack, vertexConsumer));
        context.draw();
        dispatcher.setRenderShadows(true);
        stack.pop();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(BACKGROUND, x + 20, y, 0, 0, 0, 200, 165, 200, 165);
        context.fill(x + 23, y + 3, x + 220 - 3, y + 165 - 3, 0, 0x1fffffff);
    }

    private void applyPosePointToRenderer(PlayerEntityModel<NpcEntity> model) {
        applyTransformations(model.body, model.jacket, posePoint.torso);
        applyTransformations(model.head, model.hat, posePoint.head);
        applyTransformations(model.leftArm, model.leftSleeve, posePoint.leftArm);
        applyTransformations(model.rightArm, model.rightSleeve, posePoint.rightArm);
        applyTransformations(model.leftLeg, model.leftPants, posePoint.leftLeg);
        applyTransformations(model.rightLeg, model.rightPants, posePoint.rightLeg);
    }

    private void applyTransformations(ModelPart modelPart, ModelPart modelPart2, PoseData poseData) {
        modelPart.setAngles(poseData.pitch, poseData.yaw, poseData.roll);
        modelPart.setPivot(poseData.translationX, poseData.translationY, poseData.translationZ);
        setScale(modelPart, poseData);
        modelPart2.setAngles(poseData.pitch, poseData.yaw, poseData.roll);
        modelPart2.setPivot(poseData.translationX, poseData.translationY, poseData.translationZ);
        setScale(modelPart2, poseData);
    }

    private void setScale(ModelPart modelPart, PoseData poseData){
        modelPart.xScale = poseData.scaleX;
        modelPart.yScale = poseData.scaleY;
        modelPart.zScale = poseData.scaleZ;
    }

    private void renderModel(MatrixStack stack, VertexConsumer vertexConsumer) {
        renderPart(Part.TORSO, model.body, model.jacket, stack, vertexConsumer);
        renderPart(Part.HEAD, model.head, model.hat, stack, vertexConsumer);
        renderPart(Part.LEFT_ARM, model.leftArm, model.leftSleeve, stack, vertexConsumer);
        renderPart(Part.RIGHT_ARM, model.rightArm, model.rightSleeve, stack, vertexConsumer);
        renderPart(Part.LEFT_LEG, model.leftLeg, model.leftPants, stack, vertexConsumer);
        renderPart(Part.RIGHT_LEG, model.rightLeg, model.rightPants, stack, vertexConsumer);
    }

    private void renderPart(Part part, ModelPart modelPart, ModelPart modelPart2, MatrixStack stack, VertexConsumer vertexConsumer) {
        float alpha = part == this.part ? 1f : 0.45f;
        modelPart.render(stack, vertexConsumer, 0x00f000f0, OverlayTexture.DEFAULT_UV, alpha, alpha, alpha, 1f);
        modelPart2.render(stack, vertexConsumer, 0x00f000f0, OverlayTexture.DEFAULT_UV, alpha, alpha, alpha, 1f);
    }

    private PoseData getDataFromPart(Part part) {
        return switch (part) {
            case TORSO -> posePoint.torso;
            case HEAD -> posePoint.head;
            case LEFT_ARM -> posePoint.leftArm;
            case RIGHT_ARM -> posePoint.rightArm;
            case LEFT_LEG -> posePoint.leftLeg;
            case RIGHT_LEG -> posePoint.rightLeg;
        };
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        final float precision = MathHelper.PI / 50;
        if (mouseX > x + 250 && mouseX < x + 430 && mouseY > y - 20 && mouseY < y + 185) {
            switch (button) {
                case 0 -> {
                    modelYaw += deltaX < 0 ? precision : -precision;
                    modelYaw %= MathHelper.PI * 2;
                }
                case 1 -> {
                    modelPitch += deltaY > 0 ? precision : -precision;
                    modelPitch %= MathHelper.PI * 2;
                }
            }
        }
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX > x - 50 && mouseX < x + 50 && mouseY > y - 30 && mouseY < y + 140) {
            if (button == 2) {
                modelPitch = 0;
                modelYaw = 0;
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    private enum Part {
        TORSO, HEAD, LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG
    }

    private enum Type {
        ROTATION, TRANSLATION, SCALE
    }

    private static Identifier texture(String path){
        return new Identifier(CinematicCreeper.MOD_ID, "textures/gui/" + path);
    }

}
