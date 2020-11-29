package fr.ekode.fabriclockette.entities;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockStatePos {

    private BlockState blockState;
    private BlockPos blockPos;

    public BlockStatePos(BlockState blockState, BlockPos blockPos) {
        this.blockState = blockState;
        this.blockPos = blockPos;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
