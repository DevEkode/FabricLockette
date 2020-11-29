package fr.ekode.fabriclockette.entities;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockStatePosProtected {

    private BlockState blockState;
    private BlockPos blockPos;
    private ProtectedBlock protectedBlock;

    public BlockStatePosProtected(BlockState blockState, BlockPos blockPos, ProtectedBlock protectedBlock) {
        this.blockState = blockState;
        this.blockPos = blockPos;
        this.protectedBlock = protectedBlock;
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

    public ProtectedBlock getProtectedBlock() {
        return protectedBlock;
    }

    public void setProtectedBlock(ProtectedBlock protectedBlock) {
        this.protectedBlock = protectedBlock;
    }
}
