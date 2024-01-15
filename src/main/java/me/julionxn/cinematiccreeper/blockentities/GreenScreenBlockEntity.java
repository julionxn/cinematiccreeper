package me.julionxn.cinematiccreeper.blockentities;

import me.julionxn.cinematiccreeper.util.colors.Color;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class GreenScreenBlockEntity extends BlockEntity {

    private final Color color = new Color(0x00ff00);

    public GreenScreenBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.GREEN_SCREEN_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        color.setColor(nbt.getInt("GreenScreenColor"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("GreenScreenColor", color.getColor());
    }

    public Color getColor(){
        return color;
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 0){
            color.setColor(data);
            return true;
        }
        return super.onSyncedBlockEvent(type, data);
    }
}
