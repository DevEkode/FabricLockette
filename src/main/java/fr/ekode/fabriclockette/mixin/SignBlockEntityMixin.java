package fr.ekode.fabriclockette.mixin;

import fr.ekode.fabriclockette.core.PlayerHelper;
import fr.ekode.fabriclockette.entities.BlockStatePos;
import fr.ekode.fabriclockette.enums.PrivateSignNbt;
import fr.ekode.fabriclockette.extentions.SignBlockEntityExt;
import fr.ekode.fabriclockette.managers.ContainerManager;
import fr.ekode.fabriclockette.managers.SignManager;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
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
    private boolean editable;
    @Shadow @Final private Text[] texts;
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
        return this.texts[row];
    }

    // Add setEditable function on SERVER environnement
    public void setEditableServer(boolean bl) {
        this.editable = bl;
    }

    // Inject code for retrieving owners from sign nbt tags
    @Inject(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/SignBlockEntity;parseTextFromJson(Ljava/lang/String;)Lnet/minecraft/text/Text;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void readNbt(NbtCompound tag, CallbackInfo ci, int i) {
        String tagName = PrivateSignNbt.OWNER.getNbtTag() + (i+1);

        if (i > 0 && tag.containsUuid(tagName)) {
            UUID owner = tag.getUuid(tagName);
            this.owners[i - 1] = owner;
        }
    }

    // Inject code for saving owners into sign nbt tags
    @Inject(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text$Serializer;toJson(Lnet/minecraft/text/Text;)Ljava/lang/String;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void writeNbt(NbtCompound tag, CallbackInfoReturnable<NbtCompound> cir, int i) {
        String tagName = PrivateSignNbt.OWNER.getNbtTag() + (i+1);

        if (i > 0 && this.owners[i - 1] != null) {
            tag.putUuid(tagName, this.owners[i - 1]);
        }
    }

    // Allow player to edit sign after first place
    @Inject(method = "onActivate", at = @At("HEAD"),cancellable = true)
    public void onActivate(ServerPlayerEntity player, CallbackInfoReturnable<Boolean> callback) {
        if (player.getAbilities().allowModifyWorld) {
            editable = true;
            SignBlockEntity sign = (SignBlockEntity) (Object) this;
            SignManager signManager = new SignManager(sign);
            if (signManager.isSignPrivate()) {
                BlockStatePos blockStatePos = signManager.getAttachedContainer();
                ContainerManager containerManager = new ContainerManager(sign.getWorld(), blockStatePos.getBlockPos());
                if (containerManager.isProtected() && !containerManager.isOwner(player)) {
                    PlayerHelper.sendAccessDeniedMessage(player);
                    return;
                }
            }
            player.openEditSignScreen(sign);
        }
    }
}
