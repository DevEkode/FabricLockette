package fr.ekode.fabriclockette.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface OpenSignGuiCallback {
    /**
     * Callback for opening sign GUI
     * Called before the sign GUI is opened
     * Upon result :
     * - SUCCESS cancels further processing and continues with normal shearing behavior.
     * - PASS falls back to further processing and defaults to SUCCESS if no other listeners are available
     * - FAIL cancels further processing and does not open sign GUI.
     */
    Event<OpenSignGuiCallback> EVENT = EventFactory.createArrayBacked(OpenSignGuiCallback.class,
            listeners -> (player, sign) -> {
                for (OpenSignGuiCallback listener : listeners) {
                    ActionResult result = listener.interact(player, sign);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    /**
     * When a player interact with a sign.
     * @param player The player interacting with the sign
     * @param sign The sign interacted with
     * @return Action result
     */
    ActionResult interact(PlayerEntity player, SignBlockEntity sign);
}
