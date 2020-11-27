package fr.ekode.FabricLockette.core;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ChestHelpers {
    public static BlockEntity searchSecondChestEntity(ChestBlockEntity firstChest, BlockState blockState, World world) {
        // Search neighbour container (like double chests)
        DoubleBlockProperties.Type chestType = ChestBlock.getDoubleBlockType(blockState);
        Direction chestFacing = blockState.get(ChestBlock.FACING);
        Direction secondChestDir = null;
        switch (chestFacing) { // First = RIGHT / Second = LEFT
            case EAST:
                if (chestType == DoubleBlockProperties.Type.FIRST) secondChestDir = Direction.NORTH;
                else secondChestDir = Direction.SOUTH;
                break;
            case WEST:
                if (chestType == DoubleBlockProperties.Type.FIRST) secondChestDir = Direction.SOUTH;
                else secondChestDir = Direction.NORTH;
                break;
            case NORTH:
                if (chestType == DoubleBlockProperties.Type.FIRST) secondChestDir = Direction.WEST;
                else secondChestDir = Direction.EAST;
                break;
            case SOUTH:
                if (chestType == DoubleBlockProperties.Type.FIRST) secondChestDir = Direction.EAST;
                else secondChestDir = Direction.WEST;
                break;
        }
        // Get second chest in world
        return world.getBlockEntity(firstChest.getPos().offset(secondChestDir));
    }
}
