package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import me.julionxn.cinematiccreeper.core.poses.PoseData;
import me.julionxn.cinematiccreeper.core.poses.PosePoint;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedWidget;
import me.julionxn.cinematiccreeper.util.MathHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class PosePointWidget extends ExtendedWidget {

    private final int x;
    private final int y;
    private final PosePoint posePoint;
    private Part part = Part.HEAD;
    private static final float MAX_VALUE = MathHelper.PI * 2;
    private PlayerEntityModel<NpcEntity> model;
    private float modelYaw = 0;
    private float modelPitch = 0;

    public PosePointWidget(ExtendedScreen screen, int x, int y, PosePoint posePoint) {
        super(screen);
        this.x = x;
        this.y = y;
        this.posePoint = posePoint;
    }

    @Override
    public void init() {
        int currentX = x + 70;
        if (client != null && model == null){
            EntityModelLoader loader = client.getEntityModelLoader();
            model = new PlayerEntityModel<>(loader.getModelPart(EntityModelLayers.PLAYER), false);
        }
        partButtonOf("H", currentX, y, Part.HEAD);
        partButtonOf("LA", currentX + 30, y, Part.RIGHT_ARM);
        partButtonOf("RA", currentX + 60, y, Part.LEFT_ARM);
        partButtonOf("LL", currentX + 90, y, Part.RIGHT_LEG);
        partButtonOf("RL", currentX + 120, y, Part.LEFT_LEG);
        SliderWidget yawWidget = new SliderWidget(currentX + 5, y + 30, () -> floatFromAngle(getDataFromPart(part).yaw),
                MAX_VALUE,
                Text.of("Yaw"),
                aFloat -> String.valueOf(MathHelper.toDegrees(aFloat)),
                aFloat -> {
                    PoseData data = getDataFromPart(part);
                    data.yaw = aFloat * MAX_VALUE;
                }
        );
        addDrawableChild(yawWidget);
        SliderWidget pitchWidget = new SliderWidget(currentX + 5, y + 65, () -> floatFromAngle(getDataFromPart(part).pitch),
                MAX_VALUE,
                Text.of("Pitch"),
                aFloat -> String.valueOf(MathHelper.toDegrees(aFloat)),
                aFloat -> {
                    PoseData data = getDataFromPart(part);
                    data.pitch = aFloat * MAX_VALUE;
                }
        );
        addDrawableChild(pitchWidget);
        SliderWidget rollWidget = new SliderWidget(currentX + 5, y + 100, () -> floatFromAngle(getDataFromPart(part).roll),
                MAX_VALUE,
                Text.of("Roll"),
                aFloat -> String.valueOf(MathHelper.toDegrees(aFloat)),
                aFloat -> {
                    PoseData data = getDataFromPart(part);
                    data.roll = aFloat * MAX_VALUE;
                }
        );
        addDrawableChild(rollWidget);
    }

    private void partButtonOf(String text, int x, int y, Part part){
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of(text), button -> {
                    this.part = part;
                }).dimensions(x, y, 30, 20).build();
        addDrawableChild(buttonWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (client == null) return;
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.translate(x - 5, y + 30, 200);
        stack.multiply(RotationAxis.NEGATIVE_Y.rotation(MathHelper.PI));
        stack.translate(0, 35, 0);
        stack.multiply(new Quaternionf().rotationZYX(0, modelYaw, modelPitch));
        stack.translate(0, -35, 0);
        stack.scale(70, 70, 70);
        applyPosePointToRenderer(model);
        RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(DefaultSkinHelper.getTexture());
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(renderLayer);
        renderModel(stack, vertexConsumer);
        context.draw();
        stack.pop();
    }

    private void applyPosePointToRenderer(PlayerEntityModel<NpcEntity> model){
        applyAnglesToParts(model.head, model.hat, posePoint.head);
        applyAnglesToParts(model.leftArm, model.leftSleeve, posePoint.leftArm);
        applyAnglesToParts(model.rightArm, model.rightSleeve, posePoint.rightArm);
        applyAnglesToParts(model.leftLeg, model.leftPants, posePoint.leftLeg);
        applyAnglesToParts(model.rightLeg, model.rightPants, posePoint.rightLeg);
    }

    private void applyAnglesToParts(ModelPart modelPart, ModelPart modelPart2, PoseData poseData){
        modelPart.setAngles(poseData.pitch, poseData.yaw, poseData.roll);
        modelPart2.setAngles(poseData.pitch, poseData.yaw, poseData.roll);
    }

    private void renderModel(MatrixStack stack, VertexConsumer vertexConsumer){
        renderPart(Part.HEAD, model.head, model.hat, stack, vertexConsumer);
        renderPart(Part.LEFT_ARM, model.leftArm, model.leftSleeve, stack, vertexConsumer);
        renderPart(Part.RIGHT_ARM, model.rightArm, model.rightSleeve, stack, vertexConsumer);
        renderPart(Part.LEFT_LEG, model.leftLeg, model.leftPants, stack, vertexConsumer);
        renderPart(Part.RIGHT_LEG, model.rightLeg, model.rightPants, stack, vertexConsumer);
        model.body.render(stack, vertexConsumer, 0x00f000f0, OverlayTexture.DEFAULT_UV, 0.45f, 0.45f, 0.45f, 1f);
    }

    private void renderPart(Part part, ModelPart modelPart, ModelPart modelPart2, MatrixStack stack, VertexConsumer vertexConsumer){
        float alpha = part == this.part ? 1f : 0.45f;
        modelPart.render(stack, vertexConsumer, 0x00f000f0, OverlayTexture.DEFAULT_UV, alpha, alpha, alpha,1f);
        modelPart2.render(stack, vertexConsumer, 0x00f000f0, OverlayTexture.DEFAULT_UV, alpha, alpha, alpha, 1f);
    }

    private PoseData getDataFromPart(Part part){
        return switch (part){
            case HEAD -> posePoint.head;
            case LEFT_ARM -> posePoint.leftArm;
            case RIGHT_ARM -> posePoint.rightArm;
            case LEFT_LEG -> posePoint.leftLeg;
            case RIGHT_LEG -> posePoint.rightLeg;
        };
    }

    private float floatFromAngle(float value){
        return value / (MathHelper.PI * 2);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        final float precision = MathHelper.PI / 50;
        if (mouseX > x - 50 && mouseX < x + 50 && mouseY > y - 30 && mouseY < y + 140){
            switch (button){
                case 0 -> {
                    modelYaw += deltaX > 0 ? precision : -precision;
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
        if (mouseX > x - 50 && mouseX < x + 50 && mouseY > y - 30 && mouseY < y + 140){
            if (button == 2){
                modelPitch = 0;
                modelYaw = 0;
            }
        }
        super.mouseClicked(mouseX, mouseY, button);
    }

    private enum Part {
        HEAD, LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG
    }

}
