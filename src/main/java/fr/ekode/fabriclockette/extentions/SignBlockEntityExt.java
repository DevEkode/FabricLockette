package fr.ekode.fabriclockette.extentions;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.UUID;

public interface SignBlockEntityExt {
    /**
     * Set owner of line on sign (from line 2 to 4)
     * @param row has to be between 1 and 3
     * @param userUuid user UUID
     */
    void setOwner(int row, UUID userUuid);

    /**
     * Get the owner of the line on sign (from line 2 to 4)
     * @param row has to be between 1 and 3
     * @return user UUID
     */
    UUID getOwner(int row);

    /**
     * Get the text on the provided row
     * @param row line
     * @return text of row
     */
    @Environment(EnvType.SERVER)
    Text getTextOnRow(int row);

    @Environment(EnvType.SERVER)
    void setEditable(boolean bl);
}
