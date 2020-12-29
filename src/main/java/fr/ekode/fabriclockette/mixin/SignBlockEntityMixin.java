package fr.ekode.fabriclockette.mixin;

import fr.ekode.fabriclockette.enums.PrivateSignNbt;
import fr.ekode.fabriclockette.events.OpenSignGuiCallback;
import fr.ekode.fabriclockette.extentions.SignBlockEntityExt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.UUID;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin implements SignBlockEntityExt {
    @Shadow
    @Final
    private Text[] text;
    @Shadow
    private boolean editable;
    private UUID[] owners; // Array for storing the sign owners

    // Inject array init into constructor
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.owners = new UUID[3];
    }

    /**
     * Set owner for row of sign
     * @param row has to be between 1 and 3
     * @param userUuid user UUID
     */
    public void setOwner(int row, UUID userUuid) {
        this.owners[row - 1] = userUuid;
    }

    /**
     * Get owner for row of sign
     * @param row has to be between 1 and 3
     * @return UUID of owner
     */
    public UUID getOwner(int row) {
        return this.owners[row - 1];
    }

    // Add getTextOnRow function on SERVER environnement
    public Text getTextOnRowServer(int row) {
        return this.text[row];
    }

    // Add setEditable function on SERVER environnement
    public void setEditableServer(boolean bl) {
        this.editable = bl;
    }

    // Inject code for retrieving owners from sign nbt tags
    @Inject(method = "fromTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text$Serializer;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci, int i) {
        String tagName = PrivateSignNbt.OWNER.getNbtTag() + (i+1);

        if (i > 0 && tag.containsUuid(tagName)) {
            UUID owner = tag.getUuid(tagName);
            this.owners[i - 1] = owner;
        }
    }

    // Inject code for saving owners into sign nbt tags
    @Inject(method = "toTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text$Serializer;toJson(Lnet/minecraft/text/Text;)Ljava/lang/String;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir, int i) {
        String tagName = PrivateSignNbt.OWNER.getNbtTag() + (i+1);

        if (i > 0 && this.owners[i - 1] != null) {
            tag.putUuid(tagName, this.owners[i - 1]);
        }
    }

    // Allow player to edit sign after first place
    @Inject(method = "onActivate", at = @At("HEAD"),cancellable = true)
    public void useOnBlock(PlayerEntity player, CallbackInfoReturnable<Boolean> callback) {
        if (player.abilities.allowModifyWorld) {
            editable = true;
            SignBlockEntity sign = (SignBlockEntity) (Object) this;

            // Send event
            ActionResult result = OpenSignGuiCallback.EVENT.invoker().interact(player, sign);

            if (result != ActionResult.FAIL) player.openEditSignScreen(sign);
        }
    }
}
