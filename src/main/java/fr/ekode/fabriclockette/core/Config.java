package fr.ekode.fabriclockette.core;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class Config {

    private static Config INSTANCE = null;

    private static final String CONFIG_DIR = "mods/FabricLockette/";
    private static final String CONFIG_FILE = "config.properties";
    private static final String CONFIG_FILE_DESCRIPTION = "FabricLockette config file";
    private Properties config;

    // Setup default config to create if needed
    private static final Map<String,String> DEFAULT_CONFIG;
    static {
        Map<String,String> map = new HashMap<>();
        map.put("lang","en_US");
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
                this.config = FileResourcesUtils.readPropertiesFile(CONFIG_DIR+CONFIG_FILE);
                initConfig();
                created = true;
            }
        }
        this.config = FileResourcesUtils.readPropertiesFile(CONFIG_DIR+CONFIG_FILE);
        if(!created) completeConfig();
    }

    public static Config getInstance() throws IOException {
        if(INSTANCE == null){
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    public String get(String key){
        return this.config.getProperty(key);
    }

    public void set(String key, String value){
        this.config.setProperty(key,value);
    }


    /**
     * Initialize config file with default params and save it
     * @throws IOException cannot save config file
     */
    private void initConfig() throws IOException {
        DEFAULT_CONFIG.forEach((key, value) -> this.config.put(key,value));
        saveConfig();
    }

    /**
     * Add missing params into config files and save it
     * @throws IOException cannot save config file
     */
    private void completeConfig() throws IOException {
        AtomicBoolean edited = new AtomicBoolean(false);
        DEFAULT_CONFIG.forEach((key, value) -> {
            // Check if key exists in config file
            if(!this.config.containsKey(key)){
                this.config.put(key,value);
                edited.set(true);
            }
        });

        // Save config if edited
        if(edited.get()){
            this.saveConfig();
        }
    }

    /**
     * Save config file
     * @throws IOException cannot save config file
     */
    public void saveConfig() throws IOException {
        String path = CONFIG_DIR+CONFIG_FILE;
        FileOutputStream outputStream = new FileOutputStream(path);
        this.config.store(outputStream,CONFIG_FILE_DESCRIPTION);
    }
}
