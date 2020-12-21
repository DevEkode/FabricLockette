package fr.ekode.fabriclockette.managers;

import com.mojang.authlib.GameProfile;
import fr.ekode.fabriclockette.blocks.ProtectedBlockRepository;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.*;

public class ContainerManager {

    private World world;
    private BlockPos blockPos;

    public ContainerManager(World world,BlockPos blockPos) {
        this.world = world;
        this.blockPos = blockPos;
    }

    /**
     * Search a sign with [private] indication on LockableContainerBlockEntity
     *
     * @return the sign entity attached
     */
    List<SignBlockEntity> searchPrivateSign(Map<BlockPos,Direction> availablePos) {
        List<SignBlockEntity> list = new ArrayList<>();

        //Search every sides (not up and down -> sign cannot be placed here)
        for(Map.Entry<BlockPos,Direction> entry : availablePos.entrySet()) {
            BlockPos pos = entry.getKey();
            Direction direction = entry.getValue();

            BlockState state = world.getBlockState(pos);

            if(state.getBlock() instanceof WallSignBlock){
                Direction realDirection = state.get(WallSignBlock.FACING);
                if(!realDirection.equals(direction)) continue;

                // Search first row for [private]
                SignBlockEntity sign = (SignBlockEntity) world.getBlockEntity(pos);
                SignManager signManager = new SignManager(sign);
                if(signManager.isSignPrivate()){
                    list.add(sign);
                }
            }
        }
        return list;
    }

    public boolean isProtected() {
        boolean canBeProtected = ProtectedBlockRepository.canThisBlockBeProtected(world, blockPos);
        if(!canBeProtected) return false;

        List<SignBlockEntity> list = searchPrivateSignResult();
        return !list.isEmpty();
    }

    public List<SignBlockEntity> searchPrivateSignResult(){
        //Search for [private] sign
        Map<BlockPos,Direction> blockPosList = ProtectedBlockRepository.getAvailablePrivateSignPos(this.world,this.blockPos);

        if(blockPosList == null) return Collections.emptyList();

        return this.searchPrivateSign(blockPosList);
    }

    public boolean isOwner(PlayerEntity player) {
        List<SignBlockEntity> privateSigns = searchPrivateSignResult();

        if(player != null && privateSigns != null){
            GameProfile gameProfile = player.getGameProfile();
            int opLevel = player.getServer().getOpPermissionLevel();
            int playerOpLevel = player.getServer().getPermissionLevel(gameProfile);

            if(opLevel == playerOpLevel) return true;

            for(SignBlockEntity sign : privateSigns){
                // Get owners
                SignManager signManager = new SignManager(sign);
                List<UUID> owners = signManager.getSignOwners();

                UUID playerUuid = player.getUuid();

                for(UUID owner : owners){
                    if(owner != null && owner.equals(playerUuid)) return true;
                }
            }
        }
        return false;
    }
}
