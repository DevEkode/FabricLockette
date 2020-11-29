package fr.ekode.fabriclockette.core;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class TextHelpers {

    private TextHelpers() {}

    /**
     * Remove type §_ colors or formating codes from text
     * @param rawText text with §_ codes to remove
     * @return text without §_ codes
     */
    public static Text removeMinecraftFormatingCodes(Text rawText){
        String textS = rawText.asString();
        String parsed = removeMinecraftFormatingCodes(textS);
        return new LiteralText(parsed);
    }

    public static String removeMinecraftFormatingCodes(String rawText){
        return rawText.replaceAll("§[a0-z9]","");
    }
}
