package fr.ekode.fabriclockette.managers;

import fr.ekode.fabriclockette.blocks.ProtectedBlockRepository;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

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
    SignBlockEntity searchPrivateSign(List<BlockPos> availablePos) {
        //Search every sides (not up and down -> sign cannot be placed here)
        for(BlockPos pos : availablePos){
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

    public boolean isContainerProtected(PlayerEntity player) {
        //Search for [private] sign
        List<BlockPos> blockPosList = ProtectedBlockRepository.getAvailablePrivateSignPos(player.getEntityWorld(),this.blockPos);
        SignBlockEntity privateSign = this.searchPrivateSign(blockPosList);

        if(privateSign != null){
            // Get owners
            SignManager signManager = new SignManager(privateSign);
            List<UUID> owners = signManager.getSignOwners();

            UUID playerUuid = player.getUuid();

            for(UUID owner : owners){
                if(owner != null && owner.equals(playerUuid)) return false;
            }
            return true;
        }
        return false;
    }
}
