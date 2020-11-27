package fr.ekode.FabricLockette.managers;

import fr.ekode.FabricLockette.core.ChestHelpers;
import net.minecraft.block.*;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContainerManager {

    private final LockableContainerBlockEntity container;

    public ContainerManager(LockableContainerBlockEntity container) {
        this.container = container;
    }

    /**
     * Search a sign with [private] indication on LockableContainerBlockEntity
     *
     * @return the sign entity attached
     */
    SignBlockEntity searchPrivateSign() {
        //Search every sides (not up and down -> sign cannot be placed here)
        World world = this.container.getWorld();
        for(Direction dir : Direction.values()){
            if(dir == Direction.UP || dir == Direction.DOWN) continue;

            BlockPos pos = this.container.getPos().offset(dir);
            BlockState state = world.getBlockState(pos);

            if(state.getBlock() instanceof WallSignBlock){
                // Search first row for [private]
                SignBlockEntity sign = (SignBlockEntity) world.getBlockEntity(pos);
                SignManager signManager = new SignManager(sign);
                if(signManager.isSignPrivate()) return sign;
            }
        }
        return null;
    }

    public List<LockableContainerBlockEntity> getContainers(){
        List<LockableContainerBlockEntity> containerList = new ArrayList<LockableContainerBlockEntity>();
        World world = this.container.getWorld();

        if(world != null){
            containerList.add(this.container);
            BlockState blockState = this.container.getWorld().getBlockState(this.container.getPos());
            Block block = blockState.getBlock();

            // Check if the container is a double chest
            if (block instanceof ChestBlock) {
                DoubleBlockProperties.Type chestType = ChestBlock.getDoubleBlockType(blockState);
                if (chestType != DoubleBlockProperties.Type.SINGLE) { // Search the second chest entity if double
                    containerList.add((LockableContainerBlockEntity) ChestHelpers.searchSecondChestEntity((ChestBlockEntity) this.container,blockState,world));
                }
            }

            return containerList;
        }
        return null;
    }

    public boolean isContainerProtected(PlayerEntity player) {
        //Search for [private] sign
        SignBlockEntity privateSign = this.searchPrivateSign();
        if(privateSign != null){
            // Get owners
            SignManager signManager = new SignManager(privateSign);
            List<UUID> owners = signManager.getSignOwners();

            UUID playerUuid = player.getUuid();

            for(UUID owner : owners){
                if(owner != null && owner.equals(playerUuid)) return false;
            }
        }
        return true;
    }
}
