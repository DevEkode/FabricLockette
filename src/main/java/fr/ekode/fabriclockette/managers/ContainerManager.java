package fr.ekode.fabriclockette.managers;

import com.mojang.authlib.GameProfile;
import fr.ekode.fabriclockette.blocks.ProtectedBlockRepository;
import fr.ekode.fabriclockette.core.TextHelpers;
import fr.ekode.fabriclockette.utils.ServerConfigUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ContainerManager {

    /**
     * Current world of the container.
     */
    private World world;

    /**
     * Container block position.
     */
    private BlockPos blockPos;

    /**
     * Constructor of ContainerManager.
     * @param currentWorld current word
     * @param pos container position
     */
    public ContainerManager(final World currentWorld, final BlockPos pos) {
        this.world = currentWorld;
        this.blockPos = pos;
    }

    /**
     * Search a sign with [private] indication on LockableContainerBlockEntity.
     * @param availablePos list of available position for private signs
     * @return the sign entity attached
     */
    List<SignBlockEntity> searchPrivateSign(final Map<BlockPos, Direction> availablePos) {
        List<SignBlockEntity> list = new ArrayList<>();

        //Search every sides (not up and down -> sign cannot be placed here)
        for (Map.Entry<BlockPos, Direction> entry : availablePos.entrySet()) {
            BlockPos pos = entry.getKey();
            Direction direction = entry.getValue();

            BlockState state = world.getBlockState(pos);

            if (state.getBlock() instanceof WallSignBlock) {
                Direction realDirection = state.get(WallSignBlock.FACING);
                if (!realDirection.equals(direction)) {
                    continue;
                }

                // Search first row for [private]
                SignBlockEntity sign = (SignBlockEntity) world.getBlockEntity(pos);
                SignManager signManager = new SignManager(sign);
                if (signManager.isSignPrivate()) {
                    list.add(sign);
                }
            }
        }
        return list;
    }

    /**
     * Check if this container is protected by a private sign.
     * @return true if protected, false if not
     */
    public boolean isProtected() {
        boolean canBeProtected = ProtectedBlockRepository.canThisBlockBeProtected(world, blockPos);
        if (!canBeProtected) {
            return false;
        }

        List<SignBlockEntity> list = searchPrivateSignResult();
        return !list.isEmpty();
    }

    /**
     * Search for private sign linked to this container.
     * @return A list of private signs
     */
    public List<SignBlockEntity> searchPrivateSignResult() {
        //Search for [private] sign
        Map<BlockPos, Direction> blockPosList
                = ProtectedBlockRepository.getAvailablePrivateSignPos(this.world, this.blockPos);

        if (blockPosList == null) {
            return Collections.emptyList();
        }

        return this.searchPrivateSign(blockPosList);
    }

    /**
     * Check if the player is the owner of this container.
     * @param player player to check
     * @return true if owner, false if not
     */
    public boolean isOwner(final PlayerEntity player) {
        List<SignBlockEntity> privateSigns = searchPrivateSignResult();

        if (player != null && privateSigns != null) {
            GameProfile gameProfile = player.getGameProfile();
            int opLevel = player.getServer().getOpPermissionLevel();
            int playerOpLevel = player.getServer().getPermissionLevel(gameProfile);

            if (opLevel == playerOpLevel) {
                return true;
            }

            for (SignBlockEntity sign : privateSigns) {
                SignManager signManager = new SignManager(sign);
                if(ServerConfigUtils.isOnline()){ // When the server uses Mojang auth
                    // Get owners
                    List<UUID> owners = signManager.getSignOwners();

                    UUID playerUuid = player.getUuid();

                    for (UUID owner : owners) {
                        if (owner != null && owner.equals(playerUuid)) {
                            return true;
                        }
                    }
                }else{ // When the server is not using Mojang Auth
                    List<Text> owners = signManager.getSignUsernames();
                    String playerUsername = player.getName().getString();

                    for(Text owner : owners){
                        String ownerFiltered = TextHelpers.removeMinecraftFormatingCodes(owner).getString();
                        if(ownerFiltered.equals(playerUsername)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
