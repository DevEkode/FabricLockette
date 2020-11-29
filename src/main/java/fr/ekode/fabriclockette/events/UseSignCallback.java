package fr.ekode.fabriclockette.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface UseSignCallback {

    /**
     * Callback for placing a sign
     * Called before the sign GUI is opened
     * Upon result :
     * - SUCCESS cancels further processing and continues with normal shearing behavior.
     * - PASS falls back to further processing and defaults to SUCCESS if no other listeners are available
     * - FAIL cancels further processing and does not open sign GUI.
     */
    Event<UseSignCallback> EVENT = EventFactory.createArrayBacked(UseSignCallback.class,
            (listeners) -> (player,sign) -> {
                for(UseSignCallback listener : listeners){
                    ActionResult result = listener.interact(player,sign);

                    if(result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
    });

    ActionResult interact(PlayerEntity player,SignBlockEntity sign);
}
