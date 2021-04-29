package fr.ekode.fabriclockette.events;

import fr.ekode.fabriclockette.managers.SignManager;
import net.minecraft.util.ActionResult;

public class CloseSignGui implements EventRegistrator{
    @Override
    public void register() {
        // When a player close a sign GUI
        CloseSignGuiCallback.EVENT.register(sign -> {
            SignManager signManager = new SignManager(sign);
            if(signManager.isSignPrivate() & signManager.getAttachedContainer() != null){
                signManager.formatSign();
                signManager.populateSignUuids();
            }else{
                signManager.removeSignOwners();
            }
            return ActionResult.PASS;
        });
    }
}
