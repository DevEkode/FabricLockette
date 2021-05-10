package fr.ekode.fabriclockette.core;

import fr.ekode.fabriclockette.core.lang.Lang;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public final class PlayerHelper {

    private PlayerHelper() {
    }

    /**
     * Send denied message and sound to player.
     * @param player player to send the message to
     */
    public static void sendAccessDeniedMessage(final PlayerEntity player) {
        player.sendMessage(Lang.getINSTANCE().withKey("block.fabriclockette.denied"), true);
        player.playSound(SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    /**
     * Send protected message and sound to player.
     * @param player player to send the message to
     */
    public static void sendBlockProtectedMessage(final PlayerEntity player) {
        player.sendMessage(Lang.getINSTANCE().withKey("block.fabriclockette.protected"), true);
        player.playSound(SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
