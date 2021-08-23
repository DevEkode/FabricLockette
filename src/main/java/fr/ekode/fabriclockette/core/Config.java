package fr.ekode.fabriclockette.core;

import fr.ekode.fabriclockette.blocks.ProtectedBlock;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class Config {
    private static Config INSTANCE = null;

    public static final String PROTECTED_BLOCKS_KEY = "protect_";

    private static final Path DEFAULT_CONFIG_PATH = Paths.get("./mods/FabricLockette/config.properties");
    private static final String DEFAULT_CONFIG_FILE_DESCRIPTION = "FabricLockette config file";

    // Setup default config to create if needed
    private static final Map<String,String> DEFAULT_CONFIG_DATA;
    static {
        Map<String,String> map = new HashMap<>();
        map.put("lang","en_US");
        // ProtectedBlocks config
        map.put(PROTECTED_BLOCKS_KEY+"chest","true");
        map.put(PROTECTED_BLOCKS_KEY+"door","true");
        map.put(PROTECTED_BLOCKS_KEY+"shulker_box","true");
        DEFAULT_CONFIG_DATA = Collections.unmodifiableMap(map);
    }

    private final Path configPath;
    private final String configFileDescription;

    private Config() throws IOException {
        this(DEFAULT_CONFIG_PATH);
    }

    private Config(Path configPath) throws IOException {
        this.configPath = configPath;
        this.configFileDescription = DEFAULT_CONFIG_FILE_DESCRIPTION;

        // Check if config dir exists
        if(!Files.exists(configPath.getParent())){
            Files.createDirectories(configPath.getParent());
        }

        //Check if config file exists
        boolean fileSuccess = Files.exists(configPath);
        boolean created = false;
        if(!fileSuccess){
            Files.createFile(configPath);
            // Initialize config file
            initConfig();
            created = true;
        }

        // Check and add missing fields if config file is not new
        if(!created) completeConfig();
    }

    public Path getConfigPath() {
        return configPath;
    }

    private Properties getConfig() throws IOException {
        return FileResourcesUtils.readPropertiesFile(this.configPath.toString());
    }

    public static void initInstance(Path configPath) throws IOException {
        INSTANCE = new Config(configPath);
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
        Properties props = this.getConfig();
        props.setProperty(key,value);
        saveConfig(props);
    }

    /**
     * Initialize config file with default params and save it
     * @throws IOException cannot save config file
     */
    private void initConfig() throws IOException {
        Properties props = this.getConfig();
        props.putAll(DEFAULT_CONFIG_DATA);
        saveConfig(props);
    }

    /**
     * Add missing params into config files and save it
     * @throws IOException cannot save config file
     */
    private void completeConfig() throws IOException {
        Properties props = this.getConfig();
        AtomicBoolean edited = new AtomicBoolean(false);
        DEFAULT_CONFIG_DATA.forEach((key, value) -> {
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
        FileOutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(this.configPath.toString());
            toBeSaved.store(outputStream,configFileDescription);
        }finally {
            assert outputStream != null;
            outputStream.close();
        }
    }
}
