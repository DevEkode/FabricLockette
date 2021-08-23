package fr.ekode.fabriclockette.core.lang;

import fr.ekode.fabriclockette.core.Config;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.LocaleUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Lang {

    private static Lang INSTANCE = null;

    private static final String NOT_FOUND_ERROR = "translation not found";
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
        this.bundle = ResourceBundle.getBundle("messages", locale, ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES));
    }

    public static Lang getINSTANCE() {
        if(INSTANCE == null){
            INSTANCE = new Lang();
        }
        return INSTANCE;
    }

    public Text withKey(String key){
        String text = "";
        try{
            text = this.bundle.getString(key);
        }catch(MissingResourceException e){
            text = NOT_FOUND_ERROR;
        }
        return new LiteralText(text);
    }
}
