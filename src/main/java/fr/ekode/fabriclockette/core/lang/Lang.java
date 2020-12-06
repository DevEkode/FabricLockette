package fr.ekode.fabriclockette.core.lang;

import fr.ekode.fabriclockette.core.FileResourcesUtils;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class Lang {

    private static final String NOT_FOUND_ERROR = "translation not found";
    private final ResourceBundle bundle;

    public Lang(Locale locale) {
        // Load from bundle
        this.bundle = ResourceBundle.getBundle("messages",locale);
    }

    public Lang() {
        this(Locale.ROOT);
    }

    public Text withKey(String key){
        String text = this.bundle.getString(key);
        if(text.isEmpty()) text = NOT_FOUND_ERROR;
        return new LiteralText(text);
    }
}
