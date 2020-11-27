package fr.ekode.FabricLockette.mixin;

import fr.ekode.FabricLockette.events.CloseSignGuiCallback;
import fr.ekode.FabricLockette.events.UseSignCallback;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SignType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class UseSignMixin {
    @Inject(at = @At("HEAD"), method = "openEditSignScreen", cancellable = true)
    private void init(SignBlockEntity sign, CallbackInfo ci) {
        ActionResult result = UseSignCallback.EVENT.invoker().interact((PlayerEntity)(Object)this,sign);

        if(result == ActionResult.FAIL) ci.cancel();
    }


}
