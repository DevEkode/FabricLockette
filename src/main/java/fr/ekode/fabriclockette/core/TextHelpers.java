package fr.ekode.fabriclockette.core;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public final class TextHelpers {

    private TextHelpers() {
    }

    /**
     * Remove type §_ colors or formating codes from text.
     *
     * @param rawText text with §_ codes to remove
     * @return text without §_ codes
     */
    public static Text removeMinecraftFormatingCodes(final Text rawText) {
        String textS = rawText.asString();
        String parsed = removeMinecraftFormatingCodes(textS);
        return new LiteralText(parsed);
    }

    /**
     * Remove every minecraft formating codes (who begin with §).
     * @param rawText text to remove formating codes
     * @return text without formating code
     */
    public static String removeMinecraftFormatingCodes(final String rawText) {
        return rawText.replaceAll("\\§[a0-z9]", "");
    }
}
