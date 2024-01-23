package me.julionxn.cinematiccreeper.blocks;

import com.mojang.serialization.MapCodec;
import me.julionxn.cinematiccreeper.blockentities.GreenScreenBlockEntity;
import me.julionxn.cinematiccreeper.items.AllItems;
import me.julionxn.cinematiccreeper.screen.ScreenWrappers;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GreenScreenBlock extends BlockWithEntity {

    public static final MapCodec<GreenScreenBlock> CODEC = createCodec(GreenScreenBlock::new);

    public GreenScreenBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient){
            GreenScreenBlockEntity be = (GreenScreenBlockEntity) world.getBlockEntity(pos);
            boolean shouldOpenMenu = true;
            if (hand == Hand.MAIN_HAND && player.getMainHandStack().isOf(AllItems.GREEN_SCREEN_BLOCK_ITEM)){
                shouldOpenMenu = false;
            } else if (hand == Hand.OFF_HAND && player.getOffHandStack().isOf(AllItems.GREEN_SCREEN_BLOCK_ITEM)){
                shouldOpenMenu = false;
            }
            if (be != null && shouldOpenMenu){
                ScreenWrappers.openGreenScreenMenu(pos, be.getColor().getColor());
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
