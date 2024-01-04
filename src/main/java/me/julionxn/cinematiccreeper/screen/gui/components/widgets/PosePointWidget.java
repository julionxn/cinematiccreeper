package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import me.julionxn.cinematiccreeper.entity.AllEntities;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.poses.Part;
import me.julionxn.cinematiccreeper.poses.PoseData;
import me.julionxn.cinematiccreeper.poses.PosePoint;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedScreen;
import me.julionxn.cinematiccreeper.screen.gui.components.ExtendedWidget;
import me.julionxn.cinematiccreeper.util.MathHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PosePointWidget extends ExtendedWidget {

    private final int x;
    private final int y;
    private final PosePoint posePoint;
    private Part part = Part.HEAD;
    private static final float MAX_VALUE = MathHelper.PI * 2;
    private PlayerEntityModel<NpcEntity> model;
    private float modelRoll = 0;
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
            model.head.scale(new Vector3f(-0.33f));
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
                    System.out.println(part);
                    //clear();
                }).dimensions(x, y, 30, 20).build();
        addDrawableChild(buttonWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (client == null) return;
        PlayerEntity player = client.player;
        if (player == null) return;
        World world = player.getWorld();
        NpcEntity npcEntity = AllEntities.NPC_ENTITY.create(world);
        if (npcEntity == null) return;
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.translate(x, y - 55, 200);
        stack.multiply(RotationAxis.NEGATIVE_Y.rotation((float) Math.PI));
        stack.translate(0, 120, 0);
        stack.multiply(new Quaternionf().rotationZYX(0, modelYaw, modelPitch));
        stack.translate(0, -120, 0);
        stack.scale(120, 120, 120);
        applyPosePointToRenderer(model);
        model.render(stack,
                context.getVertexConsumers().getBuffer(RenderLayer.getEntityAlpha(DefaultSkinHelper.getTexture())),
                0xffffff, 0x00000000, 1f, 1f, 1f, 1f);
        context.draw();
        stack.pop();
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
                case 2 -> {
                    modelRoll += deltaX > 0 ? precision : -precision;
                    modelRoll %= MathHelper.PI * 2;
                }
            }
        }
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void applyPosePointToRenderer(PlayerEntityModel<NpcEntity> model){
        PoseData head = posePoint.head;
        model.head.setAngles(head.pitch, head.yaw, head.roll);
        model.hat.setAngles(head.pitch, head.yaw, head.roll);
        PoseData leftArm = posePoint.leftArm;
        model.leftArm.setAngles(leftArm.pitch, leftArm.yaw, leftArm.roll);
        model.leftSleeve.setAngles(leftArm.pitch, leftArm.yaw, leftArm.roll);
        PoseData rightArm = posePoint.rightArm;
        model.rightArm.setAngles(rightArm.pitch, rightArm.yaw, rightArm.roll);
        model.rightSleeve.setAngles(rightArm.pitch, rightArm.yaw, rightArm.roll);
        PoseData leftLeg = posePoint.leftLeg;
        model.leftLeg.setAngles(leftLeg.pitch, leftLeg.yaw, leftLeg.roll);
        model.leftPants.setAngles(leftLeg.pitch, leftLeg.yaw, leftLeg.roll);
        PoseData rightLeg = posePoint.rightLeg;
        model.rightLeg.setAngles(rightLeg.pitch, rightLeg.yaw, rightLeg.roll);
        model.rightPants.setAngles(rightLeg.pitch, rightLeg.yaw, rightLeg.roll);
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

}
