package fr.ekode.fabriclockette.enums;

/**
 * Enum defining what tags you can use to set a private sign.
 */
public enum PrivateTag {
    /**
     * [Private].
     */
    PRIVATE("Private"),
    /**
     * [More Users].
     */
    MORE_USERS("More Users");

    /**
     * Value of the tag.
     */
    private final String tag;

    PrivateTag(final String tag) {
        this.tag = tag;
    }

    /**
     * Get the current tag.
     * @return Tag value
     */
    public String getTag() {
        return tag;
    }

    /**
     * Get the tag with brackets (ex : [Private]).
     * @return Tag with brackets
     */
    public String getTagWithBrackets() {
        return '[' + tag + ']';
    }
}
