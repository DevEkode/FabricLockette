package fr.ekode.FabricLockette;

import fr.ekode.FabricLockette.events.CloseSignGuiCallback;
import fr.ekode.FabricLockette.events.ContainerOpenCallback;
import fr.ekode.FabricLockette.events.UseSignCallback;
import fr.ekode.FabricLockette.managers.ContainerManager;
import fr.ekode.FabricLockette.managers.SignManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

import java.util.List;

public class FabricLockette implements ModInitializer {

    private static final Logger log = LogManager.getLogger(FabricLockette.class.getName());

    @Override
    public void onInitialize() {
        UseSignCallback.EVENT.register((player,sign) -> {
            SignManager signManager = new SignManager(sign);
            List<LockableContainerBlockEntity> container = signManager.getAttachedContainer();
            if(container != null && container.size() > 0 && !signManager.isSignPrivate()){
                signManager.createDefaultSign(player);
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        ContainerOpenCallback.EVENT.register((player, container) -> {
            ContainerManager containerManager = new ContainerManager(container);
            boolean isProtected = containerManager.isContainerProtected(player);
            if(isProtected){
                player.sendMessage(new TranslatableText("container.isLocked", container.getName()), true);
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
    }
}
