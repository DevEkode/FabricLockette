package fr.ekode.fabriclockette.core;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ChestHelpers {

    private ChestHelpers() {}

    public static Direction getDirectionOfSecondChest(Direction facing, DoubleBlockProperties.Type chestType){
        Direction secondChestDir = null;
        switch (facing) { // First = RIGHT / Second = LEFT
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
            default:
                return null;
        }
        return secondChestDir;
    }

    /**
     * Check and return the second chest entity of a double chest
     * @param firstChest the first chest entity
     * @param blockState first chest block state
     * @param world world of chest
     * @return BlockEntity corresponding to second chest entity / null if not found (TODO replace with exception)
     */
    public static ChestBlockEntity searchSecondChestEntity(ChestBlockEntity firstChest, BlockState blockState, World world) {
        // Search neighbour container (like double chests)
        DoubleBlockProperties.Type chestType = ChestBlock.getDoubleBlockType(blockState);
        Direction chestFacing = blockState.get(ChestBlock.FACING);

        Direction secondChestDir = getDirectionOfSecondChest(chestFacing,chestType);

        // Get second chest in world
        BlockEntity entity = world.getBlockEntity(firstChest.getPos().offset(secondChestDir));
        if(entity instanceof ChestBlockEntity){
            return (ChestBlockEntity) entity;
        }
        return null;
    }
}
