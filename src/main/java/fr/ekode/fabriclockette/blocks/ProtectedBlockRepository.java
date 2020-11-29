package fr.ekode.fabriclockette.blocks;

import fr.ekode.fabriclockette.entities.BlockStatePosProtected;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is used like a HUB of every ProtectedBlocks implementations
 */
public class ProtectedBlockRepository {

    private ProtectedBlockRepository() {}

    /**
     * Check if this block can be protected by placing a private sign
     * @param world block world
     * @param pos block position
     * @return true if the block can be protected
     */
    public static boolean canThisBlockBeProtected(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        return block instanceof ProtectedBlock;
    }

    /**
     * Call the getProtectedBlock of ProtectedBlock instance
     * @param world block world
     * @param pos block position in world
     * @return list of ProtectedBlocks
     */
    public static List<BlockStatePosProtected> getProtectedBlock(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        if (block instanceof ProtectedBlock) {
            ProtectedBlock protectedBlock = (ProtectedBlock) block;
            return protectedBlock.getProtectedBlock(world, pos);
        }
        return Collections.emptyList();
    }

    /**
     * Call getAvailablePrivateSignPos of ProtectedBlock instance
     * @param world block world
     * @param pos block position in world
     * @return A list of BlockPos where the private sign could be placed
     */
    public static List<BlockPos> getAvailablePrivateSignPos(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof ProtectedBlock) {
            // Get all blocks who could be protected by a private sign
            List<BlockStatePosProtected> protectedBlocks = getProtectedBlock(world, pos);
            List<BlockPos> blockPosList = new ArrayList<>();

            for (BlockStatePosProtected bspp : protectedBlocks) {
                ProtectedBlock protectedBlock = bspp.getProtectedBlock();
                BlockState blockState = bspp.getBlockState();

                // Get facing for corresponding block
                Direction facing = null;
                if (block instanceof ChestBlock) facing = blockState.get(ChestBlock.FACING);
                else if (block instanceof DoorBlock) facing = blockState.get(DoorBlock.FACING);

                List<BlockPos> list = protectedBlock.getAvailablePrivateSignPos(bspp.getBlockPos(), blockState, facing);
                blockPosList.addAll(list);
            }
            return blockPosList;
        }

        return Collections.emptyList();
    }
}
