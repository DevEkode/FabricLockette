package fr.ekode.FabricLockette.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.entity.SignBlockEntity;
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
            (listeners) -> (sign) -> {
                for(CloseSignGuiCallback listener : listeners){
                    ActionResult result = listener.interact(sign);

                    if(result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(SignBlockEntity sign);
}
