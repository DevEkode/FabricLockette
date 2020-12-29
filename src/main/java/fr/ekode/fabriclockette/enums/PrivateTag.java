package fr.ekode.fabriclockette.enums;

public enum PrivateTag {
    PRIVATE("Private"),
    MORE_USERS("More Users");

    private final String tag;

    private PrivateTag(String tag){
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getTagWithBrackets(){
        return '['+tag+']';
    }
}
