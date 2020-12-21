package fr.ekode.fabriclockette;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.blocks.ProtectedBlockRepository;
import fr.ekode.fabriclockette.core.PlayerHelper;
import fr.ekode.fabriclockette.entities.BlockStatePos;
import fr.ekode.fabriclockette.enums.PrivateTag;
import fr.ekode.fabriclockette.events.CloseSignGuiCallback;
import fr.ekode.fabriclockette.events.ContainerOpenCallback;
import fr.ekode.fabriclockette.events.OpenSignGuiCallback;
import fr.ekode.fabriclockette.events.UseSignCallback;
import fr.ekode.fabriclockette.managers.ContainerManager;
import fr.ekode.fabriclockette.managers.SignManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FabricLockette implements ModInitializer {

    @Override
    public void onInitialize() {

        // When a player place a sign
        UseSignCallback.EVENT.register((player,sign) -> {
            SignManager signManager = new SignManager(sign);
            World world = sign.getWorld();

            // Get the attached block
            BlockStatePos attachedBlock = signManager.getAttachedContainer();
            if(world != null && attachedBlock != null){ // Skip if nothing is attached
                // Check if the block can be protected
                BlockState state = world.getBlockState(sign.getPos());
                Direction facing = state.get(WallSignBlock.FACING);
                boolean canBeProtected = ProtectedBlockRepository.canThisBlockBeProtected(world,attachedBlock.getBlockPos());

                if(canBeProtected && !signManager.isSignPrivate()){
                    // Check if the sign can be place at this position
                    if(signManager.canPlacePrivateSign(world,attachedBlock.getBlockPos(),sign.getPos(),facing)){
                        ContainerManager containerManager = new ContainerManager(world,attachedBlock.getBlockPos());
                        if(containerManager.searchPrivateSignResult().size() >= 1){
                            // Create a [More users] sign because a [private] already exist for this block
                            signManager.createDefaultSign(player, PrivateTag.MORE_USERS);
                        }else{
                            // Create a [private] sign
                            signManager.createDefaultSign(player, PrivateTag.PRIVATE);
                        }
                        // Prevent sign GUI to be opened
                        return ActionResult.FAIL;
                    }
                }
            }

            return ActionResult.PASS;
        });

        // When a player open a container
        ContainerOpenCallback.EVENT.register((world, player, state, pos) -> {
            ContainerManager containerManager = new ContainerManager(world,pos);
            boolean isProtected = containerManager.isProtected();
            boolean isOwner = containerManager.isOwner(player);

            if(isProtected && !isOwner){
                PlayerHelper.sendAccessDeniedMessage(player);
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        // When a player close a sign GUI
        CloseSignGuiCallback.EVENT.register((sign) -> {
            SignManager signManager = new SignManager(sign);
            if(signManager.isSignPrivate() & signManager.getAttachedContainer() != null){
                signManager.formatSign();
                signManager.populateSignUuids();
            }
            return ActionResult.PASS;
        });

        // When a player open a sign GUI
        OpenSignGuiCallback.EVENT.register((player, sign) -> {
            SignManager signManager = new SignManager(sign);
            if(signManager.isSignPrivate()){
                BlockStatePos blockStatePos = signManager.getAttachedContainer();
                ContainerManager containerManager = new ContainerManager(sign.getWorld(),blockStatePos.getBlockPos());
                if(containerManager.isProtected() && !containerManager.isOwner(player)){
                    PlayerHelper.sendAccessDeniedMessage(player);
                    return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });

        // When a player break a block
        AttackBlockCallback.EVENT.register((playerEntity, world, hand, blockPos, direction) -> {
            BlockState state = world.getBlockState(blockPos);
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            // Prevent ProtectedBlocks to be broken by another player
            if(state != null && state.getBlock() instanceof ProtectedBlock){
                ContainerManager containerManager = new ContainerManager(world,blockPos);
                if(containerManager.isProtected() && !containerManager.isOwner(playerEntity)) return ActionResult.FAIL;
            }
            // Prevent private sign to be broken by another player
            if(blockEntity instanceof SignBlockEntity){
                SignManager signManager = new SignManager((SignBlockEntity) blockEntity);
                BlockStatePos blockStatePos = signManager.getAttachedContainer();

                if(blockStatePos != null){
                    ContainerManager containerManager = new ContainerManager(world,blockStatePos.getBlockPos());
                    boolean isProtected = containerManager.isProtected();
                    boolean isOwner = containerManager.isOwner(playerEntity);
                    if(isProtected && !isOwner) return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });
    }
}
