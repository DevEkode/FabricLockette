package fr.ekode.fabriclockette.enums;

public enum PrivateSignNbt {

    /**
     * Nbt tag for the owners of the sign.
     */
    OWNER("fabriclockette_owner");

    /**
     * Nbt tag of the sign.
     */
    private final String nbtTag;

    PrivateSignNbt(final String nbtTag) {
        this.nbtTag = nbtTag;
    }

    /**
     * Get the current nbt tag.
     * @return Nbt tag value
     */
    public String getNbtTag() {
        return nbtTag;
    }
}
