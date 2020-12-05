package fr.ekode.fabriclockette.mixin.protectedBlocks;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.entities.BlockStatePosProtected;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin implements ProtectedBlock {
    @Override
    public List<BlockStatePosProtected> getProtectedBlock(World world, BlockPos pos) {
        return Collections.emptyList();
    }

    @Override
    public Map<BlockPos, Direction> getAvailablePrivateSignPos(BlockPos pos, BlockState state, Direction facing) {
        return null;
    }
}
