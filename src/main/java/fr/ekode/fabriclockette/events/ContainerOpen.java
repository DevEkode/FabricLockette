package fr.ekode.fabriclockette.events;

import fr.ekode.fabriclockette.core.PlayerHelper;
import fr.ekode.fabriclockette.managers.ContainerManager;
import net.minecraft.util.ActionResult;

public class ContainerOpen implements EventRegistrator {

    /**
     * Register function for the event.
     */
    public void register() {
        // When a player open a container
        ContainerOpenCallback.EVENT.register((world, player, state, pos) -> {
            ContainerManager containerManager = new ContainerManager(world, pos);
            boolean isProtected = containerManager.isProtected();
            boolean isOwner = containerManager.isOwner(player);

            if (isProtected && !isOwner) {
                PlayerHelper.sendAccessDeniedMessage(player);
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }
}
