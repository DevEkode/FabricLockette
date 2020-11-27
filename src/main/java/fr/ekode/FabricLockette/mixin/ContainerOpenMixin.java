package fr.ekode.FabricLockette.mixin;

import fr.ekode.FabricLockette.events.ContainerOpenCallback;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootableContainerBlockEntity.class)
public class ContainerOpenMixin {
    @Inject(at = @At("HEAD"), method = "createMenu", cancellable = true)
    private void init(int i, PlayerInventory playerInventory, PlayerEntity playerEntity, CallbackInfoReturnable<@Nullable ScreenHandler> cir) {
        ActionResult result = ContainerOpenCallback.EVENT.invoker().interact(playerEntity,(LootableContainerBlockEntity)(Object)this);

        if(result == ActionResult.FAIL) cir.cancel();
    }
}
