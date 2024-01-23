package me.julionxn.cinematiccreeper.screen.gui.components.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import me.julionxn.cinematiccreeper.screen.gui.screens.skins.AddSavedSkinMenu;
import me.julionxn.cinematiccreeper.screen.gui.screens.skins.SkinClosetMenu;
import me.julionxn.cinematiccreeper.util.MathHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.List;
import java.util.function.Supplier;

public class SkinClosetWidget extends RemovableItemsScrollWidget {

    private static PlayerEntityModel<NpcEntity> MODEL;
    private Identifier skin;
    private float modelYaw = 0;
    private float modelPitch = 0;
    private final SkinClosetMenu skinScreen;

    public SkinClosetWidget(SkinClosetMenu screen, int x, int y, Supplier<List<RemovableScrollItem>> supplier, @Nullable Identifier skin) {
        super(screen, x, y + 20, 120, 20, 6, supplier);
        this.skinScreen = screen;
        if (skin == null){
            this.skin = DefaultSkinHelper.getTexture();
        } else {
            this.skin = skin;
        }
    }

    @Override
    public void init() {
        super.init();
        if (client == null) return;
        if (MODEL == null){
            EntityModelLoader loader = client.getEntityModelLoader();
            MODEL = new PlayerEntityModel<>(loader.getModelPart(EntityModelLayers.PLAYER), false);
        }
        ButtonWidget addButton = ButtonWidget.builder(Text.of("+"), button ->
                client.setScreen(new AddSavedSkinMenu(skinScreen))
        ).dimensions(x, y - 20, 120, 20).build();
        addDrawableChild(addButton);
    }

    public void changeSkin(Identifier skin){
        this.skin = skin;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (client == null) return;
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.translate(x + 240, y - 50 , 200);
        stack.multiply(RotationAxis.POSITIVE_X.rotation(MathHelper.PI));
        stack.translate(0, -120, 0);
        stack.multiply(new Quaternionf().rotationZYX(0, modelYaw + MathHelper.PI, modelPitch));
        stack.translate(0, 120, 0);
        stack.scale(-120, -120, -120);
        RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(skin);
        VertexConsumer vertexConsumer = context.getVertexConsumers().getBuffer(renderLayer);
        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        MODEL.head.xScale = 0.6666f;
        MODEL.head.yScale = 0.6666f;
        MODEL.head.zScale = 0.6666f;
        dispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() ->
                MODEL.render(stack, vertexConsumer, 0x00f000f0, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f)
        );
        context.draw();
        dispatcher.setRenderShadows(true);
        stack.pop();
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (mouseX >= x + 180 && mouseX <= x + 300 && mouseY >= y - 10 && mouseY <= y + 150){
            final float precision = MathHelper.PI / 50;
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

}
