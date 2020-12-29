package fr.ekode.fabriclockette.core;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class Config {

    private static Config INSTANCE = null;

    public static final String PROTECTED_BLOCKS_KEY = "protect_";

    private static final String CONFIG_DIR = "mods/FabricLockette/";
    private static final String CONFIG_FILE = "config.properties";
    private static final String CONFIG_FILE_DESCRIPTION = "FabricLockette config file";

    // Setup default config to create if needed
    private static final Map<String,String> DEFAULT_CONFIG;
    static {
        Map<String,String> map = new HashMap<>();
        map.put("lang","en_US");
        // ProtectedBlocks config
        map.put(PROTECTED_BLOCKS_KEY+"chest","true");
        map.put(PROTECTED_BLOCKS_KEY+"door","true");
        map.put(PROTECTED_BLOCKS_KEY+"shulker_box","true");
        DEFAULT_CONFIG = Collections.unmodifiableMap(map);
    }

    private Config() throws IOException {
        // Check if config dir exists
        File configDir = new File(CONFIG_DIR);
        File configFile = new File(CONFIG_DIR+CONFIG_FILE);
        boolean dirSuccess = configDir.exists();
        boolean fileSuccess = configFile.exists();
        boolean created = false;

        if(!dirSuccess){
            dirSuccess = configDir.mkdir();
        }
        //Check if config file exists
        if(dirSuccess && !fileSuccess){
            fileSuccess = configFile.createNewFile();
            // Initialize config file
            if(fileSuccess){
                initConfig();
                created = true;
            }
        }
        if(!created) completeConfig();
    }

    private Properties getConfig() throws IOException {
        return FileResourcesUtils.readPropertiesFile(CONFIG_DIR+CONFIG_FILE);
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

    public void set(String key, String value) throws IOException {
        this.getConfig().setProperty(key,value);
    }

    /**
     * Initialize config file with default params and save it
     * @throws IOException cannot save config file
     */
    private void initConfig() throws IOException {
        Properties props = this.getConfig();
        DEFAULT_CONFIG.forEach((key, value) -> props.put(key,value));
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
        String path = CONFIG_DIR+CONFIG_FILE;
        FileOutputStream outputStream = new FileOutputStream(path);
        toBeSaved.store(outputStream,CONFIG_FILE_DESCRIPTION);
    }
}
