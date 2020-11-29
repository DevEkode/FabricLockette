package fr.ekode.fabriclockette.mixin;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.entities.BlockStatePosProtected;
import fr.ekode.fabriclockette.events.ContainerOpenCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(DoorBlock.class)
public class DoorBlockMixin implements ProtectedBlock {

    @Override
    public List<BlockStatePosProtected> getProtectedBlock(World world, BlockPos pos) {
        List<BlockStatePosProtected> protectedBlocks = new ArrayList<>();
        BlockState state = world.getBlockState(pos);

        protectedBlocks.add(new BlockStatePosProtected(state,pos,this));
        return protectedBlocks;
    }

    @Override
    public List<BlockPos> getAvailablePrivateSignPos(BlockPos pos, BlockState state, Direction facing) {
        // To lock a door, the sign can be placed on the door itself or on a block above
        List<Direction> directions = new ArrayList<>();
        directions.add(facing); // Front
        directions.add(facing.getOpposite()); //Back

        // Translate directions to BlockPos
        List<BlockPos> posList = new ArrayList<>();
        for(Direction dir : directions){
            posList.add(pos.offset(dir)); // Offset = +1
        }

        // Add upper block when the DoorBlock is the UPPER part
        if(state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER){
            posList.add(pos.offset(facing).up());
            posList.add(pos.offset(facing.getOpposite()).up());
        }

        return posList;
    }

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        ActionResult result = ContainerOpenCallback.EVENT.invoker().interact(world,player,state,pos);

        if(result == ActionResult.FAIL) cir.setReturnValue(ActionResult.FAIL);
    }

}
