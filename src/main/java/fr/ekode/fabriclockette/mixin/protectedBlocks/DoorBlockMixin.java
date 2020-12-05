package fr.ekode.fabriclockette.mixin.protectedBlocks;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.entities.BlockStatePosProtected;
import fr.ekode.fabriclockette.events.ContainerOpenCallback;
import net.fabricmc.loom.util.FabricApiExtension;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<BlockPos,Direction> getAvailablePrivateSignPos(BlockPos pos, BlockState state, Direction facing) {
        // To lock a door, the sign can be placed on the door itself or on a block above
        List<Direction> directions = new ArrayList<>();
        directions.add(facing); // Front
        directions.add(facing.getOpposite()); //Back

        // Translate directions to BlockPos
        Map<BlockPos,Direction> blockPosDirectionMap = new HashMap<>();
        for(Direction dir : directions){
            blockPosDirectionMap.put(pos.offset(dir),dir);
        }

        // Add upper block when the DoorBlock is the UPPER part
        Direction opposite = facing.getOpposite();
        if(state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER){
            // add block above
            blockPosDirectionMap.put(pos.offset(facing).up(),facing);
            blockPosDirectionMap.put(pos.offset(opposite).up(),opposite);

            // add lower half
            blockPosDirectionMap.put(pos.offset(facing).down(),facing);
            blockPosDirectionMap.put(pos.offset(opposite).down(),opposite);
        }else if(state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER){
            // add upper half
            blockPosDirectionMap.put(pos.offset(facing).up(),facing);
            blockPosDirectionMap.put(pos.offset(opposite).up(),opposite);

            // add block above
            blockPosDirectionMap.put(pos.offset(facing).up(2),facing);
            blockPosDirectionMap.put(pos.offset(opposite).up(2),opposite);
        }

        return blockPosDirectionMap;
    }

    @Inject(method = "onUse", at = @At("INVOKE"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        ActionResult result = ContainerOpenCallback.EVENT.invoker().interact(world,player,state,pos);

        if(result == ActionResult.FAIL){
            state = (BlockState)state.cycle(DoorBlock.OPEN);
            world.setBlockState(pos, state, 10);
        }
    }

}
