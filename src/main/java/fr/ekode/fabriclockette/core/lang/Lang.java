package fr.ekode.fabriclockette.core.lang;

import fr.ekode.fabriclockette.core.Config;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.LocaleUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Lang {

    /**
     * Instance of the Lang class.
     */
    private static Lang instance = null;

    /**
     * String to show when any transation is not found.
     */
    private static final String NOT_FOUND_ERROR = "translation not found";
    /**
     * Resource bundle of the lang files.
     */
    private final ResourceBundle bundle;

    private Lang() {
        //Check lang in config
        Locale locale = null;
        try {
            Config config = Config.getInstance();
            locale = LocaleUtils.toLocale(config.get("lang"));
        } catch (IOException e) {
            locale = Locale.ROOT;
        }
        // Load from bundle
        this.bundle = ResourceBundle.getBundle("messages", locale,
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }

    /**
     * Get the current instance of the Lang class.
     * @return Lang instance
     */
    public static Lang getINSTANCE() {
        if (instance == null) {
            instance = new Lang();
        }
        return instance;
    }

    /**
     * Return a translation string with provided key.
     * @param key translation key
     * @return Text of the translation key
     */
    public Text withKey(final String key) {
        String text = "";
        try {
            text = this.bundle.getString(key);
        } catch (MissingResourceException e) {
            text = NOT_FOUND_ERROR;
        }
        return new LiteralText(text);
    }
}
