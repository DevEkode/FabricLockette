package fr.ekode.fabriclockette.blocks;

import fr.ekode.fabriclockette.core.Config;
import fr.ekode.fabriclockette.entities.BlockStatePosProtected;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.*;

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

        // Check in the config if the protection is enabled for this block
        if(block instanceof ProtectedBlock){
            ProtectedBlock protectedBlock = (ProtectedBlock) block;
            String configId = Config.PROTECTED_BLOCKS_KEY + protectedBlock.getLocketteId(); //protect_[block_id]

            String isProtectedInConfig = null;
            try {
                isProtectedInConfig = Config.getInstance().get(configId);
            } catch (IOException e) {
                return false;
            }
            return isProtectedInConfig != null && isProtectedInConfig.equals("true");
        }
        return false;
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
    public static Map<BlockPos,Direction> getAvailablePrivateSignPos(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof ProtectedBlock) {
            // Get all blocks who could be protected by a private sign
            List<BlockStatePosProtected> protectedBlocks = getProtectedBlock(world, pos);
            Map<BlockPos,Direction> posList = new HashMap<>();

            for (BlockStatePosProtected bspp : protectedBlocks) {
                ProtectedBlock protectedBlock = bspp.getProtectedBlock();
                BlockState blockState = bspp.getBlockState();

                // Get facing for corresponding block
                Direction facing = null;
                if (block instanceof ChestBlock) facing = blockState.get(ChestBlock.FACING);
                else if (block instanceof DoorBlock) facing = blockState.get(DoorBlock.FACING);

                Map<BlockPos,Direction> list = protectedBlock.getAvailablePrivateSignPos(bspp.getBlockPos(), blockState, facing);
                posList.putAll(list);
            }
            return posList;
        }

        return null;
    }
}
