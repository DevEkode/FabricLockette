package fr.ekode.fabriclockette.entities;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockStatePosProtected {

    /**
     * Current state of the block.
     */
    private BlockState blockState;
    /**
     * Current position of the block.
     */
    private BlockPos blockPos;
    /**
     * A protected block.
     */
    private ProtectedBlock protectedBlock;

    /**
     * Constructor of the class BlockStatePosProtected.
     *
     * @param blockState
     * @param blockPos
     * @param protectedBlock
     */
    public BlockStatePosProtected(final BlockState blockState,
                                  final BlockPos blockPos,
                                  final ProtectedBlock protectedBlock) {
        this.blockState = blockState;
        this.blockPos = blockPos;
        this.protectedBlock = protectedBlock;
    }

    /**
     * Get the block state.
     *
     * @return a block state
     */
    public BlockState getBlockState() {
        return blockState;
    }

    /**
     * Set the block state.
     *
     * @param blockState a block state
     */
    public void setBlockState(final BlockState blockState) {
        this.blockState = blockState;
    }

    /**
     * Get the block position.
     *
     * @return a block position
     */
    public BlockPos getBlockPos() {
        return blockPos;
    }

    /**
     * Set the block position.
     *
     * @param blockPos a block position
     */
    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    /**
     * Get the protected block.
     *
     * @return a protected block
     */
    public ProtectedBlock getProtectedBlock() {
        return protectedBlock;
    }

    /**
     * Set the protected block.
     *
     * @param protectedBlock a protected block
     */
    public void setProtectedBlock(final ProtectedBlock protectedBlock) {
        this.protectedBlock = protectedBlock;
    }
}
