package fr.ekode.fabriclockette.mixin.protectedBlocks;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.core.ChestHelpers;
import fr.ekode.fabriclockette.entities.BlockStatePosProtected;
import fr.ekode.fabriclockette.events.ContainerOpenCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ChestBlock.class)
public class ChestBlockMixin implements ProtectedBlock {

    @Override
    public List<BlockStatePosProtected> getProtectedBlock(World world, BlockPos pos) {
        List<BlockStatePosProtected> protectedBlocks = new ArrayList<>();

        BlockState state = world.getBlockState(pos);
        ChestBlockEntity chestBlockEntity = (ChestBlockEntity) world.getBlockEntity(pos);

        protectedBlocks.add(new BlockStatePosProtected(state,pos,this));

        // Check if the container is a double chest
        DoubleBlockProperties.Type chestType = ChestBlock.getDoubleBlockType(state);
        if (chestType != DoubleBlockProperties.Type.SINGLE) { // Search the second chest entity if double
            ChestBlockEntity entity = ChestHelpers.searchSecondChestEntity(chestBlockEntity,state,world);
            BlockState entityState = world.getBlockState(entity.getPos());
            ProtectedBlock protectedBlock = (ProtectedBlock) entityState.getBlock();

            protectedBlocks.add(new BlockStatePosProtected(entityState,entity.getPos(),protectedBlock));
        }

        return protectedBlocks;
    }

    @Override
    public Map<BlockPos,Direction> getAvailablePrivateSignPos(BlockPos pos, BlockState state, Direction facing) {
        // Ignore UP and DOWN direction because signs cannot be placed here
        List<Direction> directions = new ArrayList<>();
        directions.add(Direction.NORTH);
        directions.add(Direction.SOUTH);
        directions.add(Direction.EAST);
        directions.add(Direction.WEST);

        DoubleBlockProperties.Type chestType = ChestBlock.getDoubleBlockType(state);
        if (chestType != DoubleBlockProperties.Type.SINGLE) {
            // Get location of the other chest
            Direction sndChestDir = ChestHelpers.getDirectionOfSecondChest(facing,chestType);
            // Remove useless direction from list (because the sign cannot be placed here)
            directions.remove(sndChestDir);
        }

        // Translate directions to BlockPos
        Map<BlockPos,Direction> directionBlockPosMap = new HashMap<>();
        for(Direction dir : directions){
            directionBlockPosMap.put(pos.offset(dir),dir);
        }

        return directionBlockPosMap;
    }

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        ActionResult result = ContainerOpenCallback.EVENT.invoker().interact(world,player,state,pos);

        if(result == ActionResult.FAIL) cir.setReturnValue(ActionResult.PASS);
    }
}

