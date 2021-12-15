package fr.ekode.fabriclockette.extentions;

import net.minecraft.text.Text;

import java.util.UUID;

public interface SignBlockEntityExt {
    /**
     * Set owner of line on sign (from line 2 to 4).
     * @param row has to be between 1 and 3
     * @param userUuid user UUID
     */
    void setOwner(int row, UUID userUuid);

    /**
     * Get the owner of the line on sign (from line 2 to 4).
     * @param row has to be between 1 and 3
     * @return user UUID
     */
    UUID getOwner(int row);

    /**
     * Get the text on the provided row.
     * @param row line
     * @return text of row
     */
    Text getTextOnRowServer(int row);

    /**
     * Set the sign editable state, server side.
     * @param bl isEditable.
     */
    void setEditableServer(boolean bl);
}
