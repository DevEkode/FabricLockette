package fr.ekode.FabricLockette.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface ContainerOpenCallback {
    /**
     * Callback for opening a container
     * Called before the sign GUI is opened
     * Upon result :
     * - SUCCESS cancels further processing and continues with normal shearing behavior.
     * - PASS falls back to further processing and defaults to SUCCESS if no other listeners are available
     * - FAIL cancels further processing and does not open sign GUI.
     */
    Event<ContainerOpenCallback> EVENT = EventFactory.createArrayBacked(ContainerOpenCallback.class,
            (listeners) -> (player,container) -> {
                for(ContainerOpenCallback listener : listeners){
                    ActionResult result = listener.interact(player,container);

                    if(result != ActionResult.PASS) return result;
                }
                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player,LootableContainerBlockEntity container);
}
