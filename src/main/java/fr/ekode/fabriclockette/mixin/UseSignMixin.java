package fr.ekode.fabriclockette.mixin;

import fr.ekode.fabriclockette.events.UseSignCallback;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class UseSignMixin {
    @Inject(at = @At("HEAD"), method = "openEditSignScreen", cancellable = true)
    private void init(final SignBlockEntity sign, final CallbackInfo ci) {
        ActionResult result = UseSignCallback.EVENT.invoker().interact((PlayerEntity) (Object) this, sign);

        if (result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}
