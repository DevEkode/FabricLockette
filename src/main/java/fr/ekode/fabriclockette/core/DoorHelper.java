package fr.ekode.fabriclockette.core;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.entities.BlockStatePos;
import fr.ekode.fabriclockette.entities.BlockStatePosProtected;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DoorHelper {

    private DoorHelper() {}

    public static Direction getDirectionOfSecondDoor(Direction facing, DoorHinge hinge){
        Direction secondDoorDir = null;
        switch(facing){
            case EAST:
                if(hinge == DoorHinge.RIGHT) secondDoorDir = Direction.NORTH;
                else secondDoorDir = Direction.SOUTH;
                break;
            case WEST:
                if(hinge == DoorHinge.RIGHT) secondDoorDir = Direction.SOUTH;
                else secondDoorDir = Direction.NORTH;
                break;
            case NORTH:
                if(hinge == DoorHinge.RIGHT) secondDoorDir = Direction.WEST;
                else secondDoorDir = Direction.EAST;
                break;
            case SOUTH:
                if(hinge == DoorHinge.RIGHT) secondDoorDir = Direction.EAST;
                else secondDoorDir = Direction.WEST;
                break;
            default:
                return null;
        }
        return secondDoorDir;
    }

    public static BlockStatePosProtected searchSecondDoorBlock(BlockPos firstDoorPos, BlockState state, World world){
        DoorHinge doorHinge = state.get(DoorBlock.HINGE);
        Direction doorFacing = state.get(DoorBlock.FACING);

        Direction secondDoorDir = getDirectionOfSecondDoor(doorFacing,doorHinge);
        BlockPos secondPos = firstDoorPos.offset(secondDoorDir);
        BlockState secondState = world.getBlockState(secondPos);

        if(secondState.getBlock() instanceof DoorBlock){
            return new BlockStatePosProtected(secondState,secondPos,(ProtectedBlock) secondState.getBlock());
        }
        return null;
    }
}
