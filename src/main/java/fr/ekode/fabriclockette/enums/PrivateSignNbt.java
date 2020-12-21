package fr.ekode.fabriclockette.enums;

public enum PrivateSignNbt {

    OWNER("fabriclockette_owner");

    private String nbtTag;

    private PrivateSignNbt(String nbtTag){
        this.nbtTag = nbtTag;
    }

    public String getNbtTag() {
        return nbtTag;
    }
}
