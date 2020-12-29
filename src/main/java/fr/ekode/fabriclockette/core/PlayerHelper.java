package fr.ekode.fabriclockette.core;

import fr.ekode.fabriclockette.core.lang.Lang;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class PlayerHelper {

    private PlayerHelper() {}

    public static void sendAccessDeniedMessage(PlayerEntity player){
        player.sendMessage(Lang.getINSTANCE().withKey("block.fabriclockette.denied"), true);
        player.playSound(SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}