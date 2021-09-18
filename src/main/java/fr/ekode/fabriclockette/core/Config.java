package fr.ekode.fabriclockette.core;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class Config {
    private static Config INSTANCE = null;

    public static final String PROTECTED_BLOCKS_KEY = "protect_";

    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("FabricLockette");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("config.properties");
    private static final String CONFIG_FILE_DESCRIPTION = "FabricLockette config file";

    // Setup default config to create if needed
    private static final Map<String,String> DEFAULT_CONFIG;
    static {
        DEFAULT_CONFIG = Map.of(
                "lang", "en_US",
                // ProtectedBlocks config
                PROTECTED_BLOCKS_KEY + "chest", "true",
                PROTECTED_BLOCKS_KEY + "door", "true",
                PROTECTED_BLOCKS_KEY + "shulker_box", "true"
        );
    }

    private Config() throws IOException {
        if(!Files.exists(CONFIG_DIR)) {
            Path legacyConfigDir = FabricLoader.getInstance().getGameDir().resolve("mods/FabricLockette");
            if (Files.exists(legacyConfigDir))
                Files.move(legacyConfigDir, CONFIG_DIR);
            else
                Files.createDirectories(CONFIG_DIR);
        }
        if (Files.exists(CONFIG_FILE)) completeConfig();
        else initConfig();
    }

    private Properties getConfig() {
        if (!Files.exists(CONFIG_FILE))
            return new Properties();
        try (InputStream fis = Files.newInputStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            prop.load(fis);
            return prop;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Config getInstance() throws IOException {
        if(INSTANCE == null){
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    public String get(String key) throws IOException {
        return this.getConfig().getProperty(key);
    }

    /**
     * Initialize config file with default params and save it
     * @throws IOException cannot save config file
     */
    private void initConfig() throws IOException {
        Properties props = new Properties();
        props.putAll(DEFAULT_CONFIG);
        saveConfig(props);
    }

    /**
     * Add missing params into config files and save it
     * @throws IOException cannot save config file
     */
    private void completeConfig() throws IOException {
        Properties props = this.getConfig();
        AtomicBoolean edited = new AtomicBoolean(false);
        DEFAULT_CONFIG.forEach((key, value) -> {
            // Check if key exists in config file
            if(!props.containsKey(key)){
                props.put(key,value);
                edited.set(true);
            }
        });

        // Save config if edited
        if(edited.get()){
            this.saveConfig(props);
        }
    }

    /**
     * Save config file
     * @throws IOException cannot save config file
     */
    public void saveConfig(Properties toBeSaved) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(CONFIG_FILE)) {
            toBeSaved.store(outputStream, CONFIG_FILE_DESCRIPTION);
        }
    }
}
