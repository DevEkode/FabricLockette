package fr.ekode.fabriclockette.events;

import fr.ekode.fabriclockette.core.PlayerHelper;
import fr.ekode.fabriclockette.entities.BlockStatePos;
import fr.ekode.fabriclockette.managers.ContainerManager;
import fr.ekode.fabriclockette.managers.SignManager;
import net.minecraft.util.ActionResult;

public class CloseSignGui implements EventRegistrator {

    /**
     * Register function for the event.
     */
    @Override
    public void register() {
        // When a player close a sign GUI
        CloseSignGuiCallback.EVENT.register((sign, player) -> {
            // Skip if sign is not private
            SignManager signManager = new SignManager(sign);
            if(!signManager.isSignPrivate()) return ActionResult.PASS;

            // Skip if container does not exists
            BlockStatePos container = signManager.getAttachedContainer();
            if(container == null) return ActionResult.PASS;

            // Check if player own this container or if it's not protected
            ContainerManager containerManager = new ContainerManager(sign.getWorld(),container.getBlockPos());
            if(containerManager.isOwner(player) || !containerManager.isProtected()){
                // Success
                signManager.populateSignUuids();

                if (signManager.hasOwners()) {
                    signManager.formatSign();
                    PlayerHelper.sendBlockProtectedMessage(player);
                }
            }else{
                // Error
                signManager.removeSignOwners();
                // Destroy sign
                sign.getWorld().breakBlock(sign.getPos(),true);
                PlayerHelper.sendAccessDeniedMessage(player);
            }

            return ActionResult.PASS;
        });
    }
}
