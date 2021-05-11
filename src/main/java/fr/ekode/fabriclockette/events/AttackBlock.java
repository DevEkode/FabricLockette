package fr.ekode.fabriclockette.events;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.entities.BlockStatePos;
import fr.ekode.fabriclockette.managers.ContainerManager;
import fr.ekode.fabriclockette.managers.SignManager;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.ActionResult;

public class AttackBlock implements EventRegistrator {

    /**
     * Register function for the event.
     */
    @Override
    public void register() {
        // When a player break a block
        AttackBlockCallback.EVENT.register((playerEntity, world, hand, blockPos, direction) -> {
            BlockState state = world.getBlockState(blockPos);
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            // Prevent ProtectedBlocks to be broken by another player
            if (state != null && state.getBlock() instanceof ProtectedBlock) {
                ContainerManager containerManager = new ContainerManager(world, blockPos);
                if (containerManager.isProtected() && !containerManager.isOwner(playerEntity)) {
                    return ActionResult.FAIL;
                }
            }
            // Prevent private sign to be broken by another player
            if (blockEntity instanceof SignBlockEntity) {
                SignManager signManager = new SignManager((SignBlockEntity) blockEntity);
                BlockStatePos blockStatePos = signManager.getAttachedContainer();

                if (blockStatePos != null) {
                    ContainerManager containerManager = new ContainerManager(world, blockStatePos.getBlockPos());
                    boolean isProtected = containerManager.isProtected();
                    boolean isOwner = containerManager.isOwner(playerEntity);
                    if (isProtected && !isOwner) {
                        return ActionResult.FAIL;
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}
