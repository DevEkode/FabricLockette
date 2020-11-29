package fr.ekode.fabriclockette;

import fr.ekode.fabriclockette.blocks.ProtectedBlockRepository;
import fr.ekode.fabriclockette.entities.BlockStatePos;
import fr.ekode.fabriclockette.events.CloseSignGuiCallback;
import fr.ekode.fabriclockette.events.ContainerOpenCallback;
import fr.ekode.fabriclockette.events.OpenSignGuiCallback;
import fr.ekode.fabriclockette.events.UseSignCallback;
import fr.ekode.fabriclockette.managers.ContainerManager;
import fr.ekode.fabriclockette.managers.SignManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class FabricLockette implements ModInitializer {

    @Override
    public void onInitialize() {
        UseSignCallback.EVENT.register((player,sign) -> {
            SignManager signManager = new SignManager(sign);
            World world = sign.getWorld();

            BlockStatePos attachedBlock = signManager.getAttachedContainer();
            if(world != null && attachedBlock != null){
                boolean canBeProtected = ProtectedBlockRepository.canThisBlockBeProtected(world,attachedBlock.getBlockPos());
                if(canBeProtected && !signManager.isSignPrivate()){

                    List<BlockPos> blockPosList = ProtectedBlockRepository.getAvailablePrivateSignPos(world,attachedBlock.getBlockPos());
                    if(blockPosList.contains(sign.getPos())){
                        signManager.createDefaultSign(player);
                        return ActionResult.FAIL;
                    }
                }
            }

            return ActionResult.PASS;
        });

        ContainerOpenCallback.EVENT.register((world, player, state, pos) -> {
            ContainerManager containerManager = new ContainerManager(world,pos);
            boolean isProtected = containerManager.isContainerProtected(player);
            if(isProtected){
                // TODO change message when locked
                player.sendMessage(new TranslatableText("container.isLocked", "Chest"), true);
                player.playSound(SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        CloseSignGuiCallback.EVENT.register((sign) -> {
            SignManager signManager = new SignManager(sign);
            if(signManager.isSignPrivate() & signManager.getAttachedContainer() != null){
                signManager.formatSign();
                signManager.populateSignUuids();
            }
            return ActionResult.PASS;
        });

        OpenSignGuiCallback.EVENT.register((player, sign) -> {
            //TODO add fail to open sign when not owner
            return ActionResult.PASS;
        });

        UseBlockCallback.EVENT.register((playerEntity, world, hand, blockHitResult) -> {
            BlockEntity entity = world.getBlockEntity(blockHitResult.getBlockPos());
            BlockState state = world.getBlockState(blockHitResult.getBlockPos());
            Block block = state.getBlock();
            return ActionResult.PASS;
        });
    }
}
