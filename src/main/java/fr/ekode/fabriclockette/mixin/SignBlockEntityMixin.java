package fr.ekode.fabriclockette.mixin;

import fr.ekode.fabriclockette.enums.PrivateSignNbt;
import fr.ekode.fabriclockette.events.OpenSignGuiCallback;
import fr.ekode.fabriclockette.extentions.SignBlockEntityExt;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
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
    /**
     * Shadow of the text variable of the SignBlockEntity class.
     */
    @Shadow
    @Final
    private Text[] texts;
    /**
     * Shadow of the boolean of the SignBlockEntity class.
     */
    @Shadow
    private boolean editable;
    /**
     * List of owners.
     */
    private UUID[] owners; // Array for storing the sign owners
    /**
     * Constant of the number of lines a sign has (for usernames).
     */
    private static final int NB_SIGN_LINES = 3;

    // Inject array init into constructor
    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(final CallbackInfo ci) {
        this.owners = new UUID[NB_SIGN_LINES];
    }

    /**
     * Set owner for row of sign.
     *
     * @param row      has to be between 1 and 3
     * @param userUuid user UUID
     */
    public void setOwner(final int row, final UUID userUuid) {
        this.owners[row - 1] = userUuid;
    }

    /**
     * Get owner for row of sign.
     *
     * @param row has to be between 1 and 3
     * @return UUID of owner
     */
    public UUID getOwner(final int row) {
        return this.owners[row - 1];
    }

    /**
     * Add getTextOnRow function on SERVER environment.
     *
     * @param row line number
     * @return Text on row
     */
    public Text getTextOnRowServer(final int row) {
        return this.texts[row];
    }

    /**
     * Add setEditable function on SERVER environment.
     *
     * @param bl isEditable.
     */
    public void setEditableServer(final boolean bl) {
        this.editable = bl;
    }

    // Inject code for retrieving owners from sign nbt tags
    @Inject(method = "readNbt", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/entity/SignBlockEntity;parseTextFromJson(Ljava/lang/String;)Lnet/minecraft/text/Text;",
            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void readNbt(NbtCompound nbt, CallbackInfo ci, int i, String string) {
        String tagName = PrivateSignNbt.OWNER.getNbtTag() + (i + 1);

        if (i > 0 && nbt.containsUuid(tagName)) {
            UUID owner = nbt.getUuid(tagName);
            this.owners[i-1] = owner;
        }
    }

    // Inject code for saving owners into sign nbt tags
    @Inject(method = "writeNbt", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serializer;toJson(Lnet/minecraft/text/Text;)Ljava/lang/String;",
            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void writeNbt(NbtCompound nbt, CallbackInfo ci, int i, Text text) {
        String tagName = PrivateSignNbt.OWNER.getNbtTag() + (i + 1);

        if (i > 0 && this.owners[i-1] != null) {
            nbt.putUuid(tagName, this.owners[i-1]);
        }
    }

    /**
     * Allow player to edit sign after first place.
     * @param player Editing player
     * @param cir callback of the event
     */
    @Inject(method = "onActivate", at = @At("HEAD"), cancellable = true)
    public void useOnBlock(ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (player.getAbilities().allowModifyWorld) {
            editable = true;
            SignBlockEntity sign = (SignBlockEntity) (Object) this;

            // Send event
            ActionResult result = OpenSignGuiCallback.EVENT.invoker().interact(player, sign);

            if (result != ActionResult.FAIL) {
                player.openEditSignScreen(sign);
            }
        }
    }
}
