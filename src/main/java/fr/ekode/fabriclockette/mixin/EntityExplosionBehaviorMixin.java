package fr.ekode.fabriclockette.mixin;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.managers.ContainerManager;
import fr.ekode.fabriclockette.managers.SignManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityExplosionBehavior.class)
public class EntityExplosionBehaviorMixin {

    @Inject(method = "canDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void canDestroyBlock(final Explosion explosion,
                                 final BlockView world,
                                 final BlockPos pos,
                                 final BlockState state,
                                 final float power,
                                 final CallbackInfoReturnable<Boolean> cir) {
        // Protect ProtectedBlocks against explosions
        if (state.getBlock() instanceof ProtectedBlock) {
            ContainerManager containerManager = new ContainerManager((World) world, pos);
            if (containerManager.isProtected()) {
                cir.setReturnValue(false);
            }
        }

        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof SignBlockEntity signBlockEntity) {
            SignManager signManager = new SignManager(signBlockEntity);
            if (signManager.isSignPrivate()) {
                cir.setReturnValue(false);
            }
        }
    }
}
