package fr.ekode.FabricLockette.mixin;

import fr.ekode.FabricLockette.extentions.SignBlockEntityExt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
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
    private UUID[] owners;


    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.owners = new UUID[3];
    }

    public void setOwner(int row, UUID userUuid) {
        this.owners[row - 1] = userUuid;
    }

    public UUID getOwner(int row) {
        return this.owners[row - 1];
    }

    @Environment(EnvType.SERVER)
    public Text getTextOnRow(int row) {
        return this.text[row];
    }

    @Environment(EnvType.SERVER)
    public void setEditable(boolean bl) {
        this.editable = bl;
    }

    @Inject(method = "fromTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text$Serializer;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci, int i) {
        if (i > 0) {
            if (tag.containsUuid("Owner" + (i + 1))) {
                UUID owner = tag.getUuid("Owner" + (i + 1));
                this.owners[i - 1] = owner;
            }
        }
    }

    @Inject(method = "toTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text$Serializer;toJson(Lnet/minecraft/text/Text;)Ljava/lang/String;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir, int i) {
        if (i > 0) {
            if (this.owners[i - 1] != null) {
                tag.putUuid("Owner" + (i + 1), this.owners[i - 1]);
            }
        }
    }

    @Inject(method = "onActivate", at = @At("HEAD"))
    public void useOnBlock(PlayerEntity player, CallbackInfoReturnable<Boolean> callback){
        if(player.abilities.allowModifyWorld){
            editable = true;
            SignBlockEntity sign = (SignBlockEntity) (Object) this;
            player.openEditSignScreen(sign);
        }
    }
}
