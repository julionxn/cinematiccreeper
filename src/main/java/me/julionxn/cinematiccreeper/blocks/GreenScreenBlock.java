package me.julionxn.cinematiccreeper.blocks;

import me.julionxn.cinematiccreeper.blockentities.GreenScreenBlockEntity;
import me.julionxn.cinematiccreeper.screen.gui.screens.ChangeGreenScreenColor;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GreenScreenBlock extends BlockWithEntity {

    public GreenScreenBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient){
            GreenScreenBlockEntity be = (GreenScreenBlockEntity) world.getBlockEntity(pos);
            if (be != null){
                MinecraftClient.getInstance().setScreen(new ChangeGreenScreenColor(pos, be.getColor().getColor()));
                return ActionResult.SUCCESS;
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GreenScreenBlockEntity(pos, state);
    }
}
