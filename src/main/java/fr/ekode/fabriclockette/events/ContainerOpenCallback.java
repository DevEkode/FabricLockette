package fr.ekode.fabriclockette.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
            (listeners) -> (world, player, blockState, pos) -> {
                for (ContainerOpenCallback listener : listeners) {
                    ActionResult result = listener.interact(world, player, blockState, pos);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    /**
     * When a player interact with a block.
     * @param world Current world
     * @param player Player doing the action
     * @param blockState State of the block interacted with
     * @param pos Position of the block interacted with
     * @return The result of the action
     */
    ActionResult interact(World world, PlayerEntity player, BlockState blockState, BlockPos pos);
}
