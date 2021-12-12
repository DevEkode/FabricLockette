package fr.ekode.fabriclockette.events;

import fr.ekode.fabriclockette.core.PlayerHelper;
import fr.ekode.fabriclockette.entities.BlockStatePos;
import fr.ekode.fabriclockette.managers.ContainerManager;
import fr.ekode.fabriclockette.managers.SignManager;
import net.minecraft.util.ActionResult;

public class OpenSignGui implements EventRegistrator {

    /**
     * Register function for the event.
     */
    public void register() {
        // When a player open a sign GUI
        OpenSignGuiCallback.EVENT.register((player, sign) -> {
            SignManager signManager = new SignManager(sign);
            if (signManager.isSignPrivate()) {
                BlockStatePos blockStatePos = signManager.getAttachedContainer();
                if(blockStatePos != null){
                    ContainerManager containerManager = new ContainerManager(sign.getWorld(), blockStatePos.getBlockPos());
                    if (containerManager.isProtected() && !containerManager.isOwner(player)) {
                        PlayerHelper.sendAccessDeniedMessage(player);
                        return ActionResult.FAIL;
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}
