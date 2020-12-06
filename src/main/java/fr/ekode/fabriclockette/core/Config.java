package fr.ekode.fabriclockette.core;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {

    private static final String CONFIG_DIR = "mods/FabricLockette/";
    private static final String CONFIG_FILE = "config.properties";
    private Properties config;

    // Setup default config to create if needed
    private static final Map<String,String> DEFAULT_CONFIG;
    static {
        Map<String,String> map = new HashMap<>();
        map.put("lang","en_US");
        DEFAULT_CONFIG = Collections.unmodifiableMap(map);
    }

    public Config() throws IOException {
        // Check if config dir exists
        File configDir = new File(CONFIG_DIR);
        File configFile = new File(CONFIG_DIR+CONFIG_FILE);
        boolean dirSuccess = configDir.exists();
        boolean fileSuccess = configFile.exists();

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
            }
        }
        this.config = FileResourcesUtils.readPropertiesFile(CONFIG_DIR+CONFIG_FILE);
    }

    private void initConfig() throws IOException {
        DEFAULT_CONFIG.forEach((key, value) -> this.config.put(key,value));
        saveConfig();
    }

    private void saveConfig() throws IOException {
        String path = CONFIG_DIR+CONFIG_FILE;
        FileOutputStream outputStream = new FileOutputStream(path);
        this.config.store(outputStream,"test");
    }
}
