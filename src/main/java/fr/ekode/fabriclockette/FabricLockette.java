package fr.ekode.fabriclockette;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.blocks.ProtectedBlockRepository;
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
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;

import java.util.List;
import java.util.Map;

public class FabricLockette implements ModInitializer {

    @Override
    public void onInitialize() {
        UseSignCallback.EVENT.register((player,sign) -> {
            SignManager signManager = new SignManager(sign);
            World world = sign.getWorld();

            BlockStatePos attachedBlock = signManager.getAttachedContainer();
            if(world != null && attachedBlock != null){
                BlockState state = world.getBlockState(sign.getPos());
                Direction facing = state.get(WallSignBlock.FACING);
                boolean canBeProtected = ProtectedBlockRepository.canThisBlockBeProtected(world,attachedBlock.getBlockPos());

                if(canBeProtected && !signManager.isSignPrivate()){
                    if(signManager.canPlacePrivateSign(world,attachedBlock.getBlockPos(),sign.getPos(),facing)){
                        ContainerManager containerManager = new ContainerManager(world,attachedBlock.getBlockPos());
                        if(containerManager.searchPrivateSignResult().size() >= 1){
                            signManager.createDefaultSign(player, PrivateTag.MORE_USERS);
                        }else{
                            signManager.createDefaultSign(player, PrivateTag.PRIVATE);
                        }

                        return ActionResult.FAIL;
                    }
                }
            }

            return ActionResult.PASS;
        });

        ContainerOpenCallback.EVENT.register((world, player, state, pos) -> {
            ContainerManager containerManager = new ContainerManager(world,pos);
            boolean isProtected = containerManager.isProtected();
            boolean isOwner = containerManager.isOwner(player);

            if(isProtected && !isOwner){
                // TODO change message when locked
                player.sendMessage(new LiteralText("C'est marqué [private], tu sais lire non ?"), true);
                player.playSound(SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
            SignManager signManager = new SignManager(sign);
            if(signManager.isSignPrivate()){
                BlockStatePos blockStatePos = signManager.getAttachedContainer();
                ContainerManager containerManager = new ContainerManager(sign.getWorld(),blockStatePos.getBlockPos());
                if(!containerManager.isOwner(player)){
                    player.sendMessage(new LiteralText("C'est marqué [private], tu sais lire non ?"), true);
                    player.playSound(SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return ActionResult.FAIL;
                }
            }
            return ActionResult.PASS;
        });


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
