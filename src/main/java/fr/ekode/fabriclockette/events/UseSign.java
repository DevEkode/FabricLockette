package fr.ekode.fabriclockette.events;

import fr.ekode.fabriclockette.blocks.ProtectedBlockRepository;
import fr.ekode.fabriclockette.core.PlayerHelper;
import fr.ekode.fabriclockette.entities.BlockStatePos;
import fr.ekode.fabriclockette.enums.PrivateTag;
import fr.ekode.fabriclockette.managers.ContainerManager;
import fr.ekode.fabriclockette.managers.SignManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class UseSign implements EventRegistrator {

    /**
     * Register function for the event.
     */
    @Override
    public void register() {
        UseSignCallback.EVENT.register((player, sign) -> {
            SignManager signManager = new SignManager(sign);
            World world = sign.getWorld();

            // Get the attached block
            BlockStatePos attachedBlock = signManager.getAttachedContainer();
            if (world != null && attachedBlock != null) { // Skip if nothing is attached
                // Check if the block can be protected
                BlockState state = world.getBlockState(sign.getPos());
                Direction facing = state.get(WallSignBlock.FACING);
                BlockPos pos = attachedBlock.getBlockPos();
                boolean canBeProtected = ProtectedBlockRepository.canThisBlockBeProtected(world, pos);

                if (canBeProtected && !signManager.isSignPrivate()) {
                    // Check if the sign can be place at this position
                    if (signManager.canPlacePrivateSign(world, attachedBlock.getBlockPos(), sign.getPos(), facing)) {
                        ContainerManager containerManager = new ContainerManager(world, attachedBlock.getBlockPos());
                        if (containerManager.searchPrivateSignResult().size() >= 1) {
                            // Create a [More users] sign because a [private] already exist for this block
                            signManager.createDefaultSign(player, PrivateTag.MORE_USERS);
                        } else {
                            // Create a [private] sign
                            signManager.createDefaultSign(player, PrivateTag.PRIVATE);
                        }
                        PlayerHelper.sendBlockProtectedMessage(player);
                        // Prevent sign GUI to be opened
                        return ActionResult.FAIL;
                    }
                }
            }

            return ActionResult.PASS;
        });
    }
}
