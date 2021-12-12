package fr.ekode.fabriclockette.core;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public final class ChestHelpers {

    private ChestHelpers() {
    }

    /**
     * Return the direction of the attached (second) chest.
     *
     * @param facing    Facing direction of the first chest
     * @param chestType ChestType of the first chest
     * @return Facing direction of the attached chest
     */
    public static Direction getDirectionOfSecondChest(final Direction facing,
                                                      final DoubleBlockProperties.Type chestType) {
        if (facing == Direction.DOWN || facing == Direction.UP) {
            return null;
        }

        if (chestType == DoubleBlockProperties.Type.FIRST) {
            return facing.rotateYCounterclockwise();
        } else {
            return facing.rotateYClockwise();
        }
    }

    /**
     * Check and return the second chest entity of a double chest.
     *
     * @param firstChest the first chest entity
     * @param blockState first chest block state
     * @param world      world of chest
     * @return BlockEntity corresponding to second chest entity / null if not found (TODO replace with exception)
     */
    public static ChestBlockEntity searchSecondChestEntity(final ChestBlockEntity firstChest,
                                                           final BlockState blockState,
                                                           final World world) {
        // Search neighbour container (like double chests)
        DoubleBlockProperties.Type chestType = ChestBlock.getDoubleBlockType(blockState);
        Direction chestFacing = blockState.get(ChestBlock.FACING);

        Direction secondChestDir = getDirectionOfSecondChest(chestFacing, chestType);

        // Get second chest in world
        BlockEntity entity = world.getBlockEntity(firstChest.getPos().offset(secondChestDir));
        if (entity instanceof ChestBlockEntity chestBlockEntity) {
            return chestBlockEntity;
        }
        return null;
    }
}
