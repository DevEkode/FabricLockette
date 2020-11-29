package fr.ekode.fabriclockette.blocks;

import fr.ekode.fabriclockette.entities.BlockStatePosProtected;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public interface ProtectedBlock {

    // TODO remove because it's useless to know what containers are protected (because we search for the sign when the player try to use it)
    /**
     * Return a list of ProtectedBlock
     * - 2 ChestBlock for double chest
     * - 2 DoorBlock for door
     * - etc ...
     * @return
     */
    public List<BlockStatePosProtected> getProtectedBlock(World world, BlockPos pos);

    /**
     * Return the list of direction where the private sign could be placed
     * @param state the current state of the block
     * @param facing the facing direction of the block
     * @return A list of direction where the private sign could be placed
     */
    public List<BlockPos> getAvailablePrivateSignPos(BlockPos pos, BlockState state, Direction facing);
}
