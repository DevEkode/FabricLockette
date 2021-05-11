package fr.ekode.fabriclockette.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface CloseSignGuiCallback {
    /**
     * Callback for closing sign GUI
     * Called after the GUI is closed and return SignEntityBlock edited
     * Upon result :
     * - SUCCESS cancels further processing and continues with normal shearing behavior.
     * - PASS falls back to further processing and defaults to SUCCESS if no other listeners are available
     * - FAIL cancels further processing (does nothing in this case)
     */
    Event<CloseSignGuiCallback> EVENT = EventFactory.createArrayBacked(CloseSignGuiCallback.class,
            listeners -> (sign, player) -> {
                for (CloseSignGuiCallback listener : listeners) {
                    ActionResult result = listener.interact(sign, player);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    /**
     * When the player interact with a sign.
     * @param sign interacted sign
     * @param player player who interacted
     * @return Return of this action
     */
    ActionResult interact(SignBlockEntity sign, ServerPlayerEntity player);
}
