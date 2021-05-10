package fr.ekode.fabriclockette.blocks.mixins;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.entities.BlockStatePosProtected;
import fr.ekode.fabriclockette.events.ContainerOpenCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin implements ProtectedBlock {
    @Override
    public final List<BlockStatePosProtected> getProtectedBlock(final World world, final BlockPos pos) {
        List<BlockStatePosProtected> protectedList = new ArrayList<>();

        BlockState state = world.getBlockState(pos);

        protectedList.add(new BlockStatePosProtected(state, pos, this));
        return protectedList;
    }

    @Override
    public final Map<BlockPos, Direction>
        getAvailablePrivateSignPos(final BlockPos pos, final BlockState state, final Direction facing) {
        List<Direction> directions = new ArrayList<>();
        directions.add(Direction.NORTH);
        directions.add(Direction.SOUTH);
        directions.add(Direction.EAST);
        directions.add(Direction.WEST);

        Map<BlockPos, Direction> directionMap = new HashMap<>();
        for (Direction d : directions) {
            directionMap.put(pos.offset(d), d);
        }

        return directionMap;
    }

    /**
     * Get FabricLockette ID for nbt tags.
     * @return string id
     */
    @Override
    public String getLocketteId() {
        return "shulker_box";
    }

    @SuppressWarnings({"LineLength", "FinalParameters"})
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ActionResult result = ContainerOpenCallback.EVENT.invoker().interact(world, player, state, pos);

        if (result == ActionResult.FAIL) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
