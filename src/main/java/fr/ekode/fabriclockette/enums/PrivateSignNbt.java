package fr.ekode.fabriclockette.enums;

public enum PrivateSignNbt {

    OWNER("Owner");

    private String nbtTag;

    private PrivateSignNbt(String nbtTag){
        this.nbtTag = nbtTag;
    }

    public String getNbtTag() {
        return nbtTag;
    }
}
