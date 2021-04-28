package fr.ekode.fabriclockette.mixin;

import fr.ekode.fabriclockette.events.CloseSignGuiCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ServerPlayNetworkHandler.class)
public class CloseSignEditScreenMixin {

    @Inject(method = "method_31282",locals= LocalCapture.CAPTURE_FAILSOFT,cancellable = true, at = @At(value = "INVOKE", target="Lnet/minecraft/server/world/ServerWorld;updateListeners(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;I)V"))
    private void onSignUpdate(UpdateSignC2SPacket updateSignC2SPacket, List list, CallbackInfo ci, ServerWorld serverWorld, BlockPos blockPos, BlockState blockState, SignBlockEntity signBlockEntity){
        ActionResult result = CloseSignGuiCallback.EVENT.invoker().interact(signBlockEntity);

        if(result == ActionResult.FAIL) ci.cancel();
    }
}
