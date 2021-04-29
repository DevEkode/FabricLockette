package fr.ekode.fabriclockette.events;

import fr.ekode.fabriclockette.core.PlayerHelper;
import fr.ekode.fabriclockette.managers.SignManager;
import net.minecraft.util.ActionResult;

public class CloseSignGui implements EventRegistrator{
    @Override
    public void register() {
        // When a player close a sign GUI
        CloseSignGuiCallback.EVENT.register((sign,player) -> {
            SignManager signManager = new SignManager(sign);

            if(signManager.isSignPrivate() && signManager.getAttachedContainer() != null){
                signManager.populateSignUuids();

                if(signManager.hasOwners()){
                    signManager.formatSign();
                    PlayerHelper.sendBlockProtectedMessage(player);
                }
            }else{
                signManager.removeSignOwners();
            }
            return ActionResult.PASS;
        });
    }
}
