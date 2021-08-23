package fr.ekode.fabriclockette.entities;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockStatePos {
    /**
     * State of the current block.
     */
    private BlockState blockState;

    /**
     * Position of the current block.
     */
    private BlockPos blockPos;

    /**
     * Constructor of BlockStatePos.
     * @param blockState
     * @param blockPos
     */
    public BlockStatePos(final BlockState blockState, final BlockPos blockPos) {
        this.blockState = blockState;
        this.blockPos = blockPos;
    }

    /**
     * Get the state of the block.
     * @return block state
     */
    public BlockState getBlockState() {
        return blockState;
    }

    /**
     * Set the state of the block.
     * @param blockState state to set
     */
    public void setBlockState(final BlockState blockState) {
        this.blockState = blockState;
    }

    /**
     * Get the position of the block.
     * @return the block position
     */
    public BlockPos getBlockPos() {
        return blockPos;
    }

    /**
     * Set the position of the block.
     * @param blockPos
     */
    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
