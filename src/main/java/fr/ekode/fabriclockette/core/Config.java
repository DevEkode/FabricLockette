package fr.ekode.fabriclockette.core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Config {
    /**
     * Instance of the current class.
     */
    private static Config instance = null;

    /**
     * Protected block key for nbt tags.
     */
    public static final String PROTECTED_BLOCKS_KEY = "protect_";

    /**
     * Default config path for configuration file.
     */
    private static final Path DEFAULT_CONFIG_PATH = Paths.get("./mods/FabricLockette/config.properties");
    /**
     * Default config file description.
     */
    private static final String DEFAULT_CONFIG_FILE_DESCRIPTION = "FabricLockette config file";

    /**
     * Map with default config data (for first creation and completion).
     */
    private static final Map<String, String> DEFAULT_CONFIG_DATA;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("lang", "en_US");
        // ProtectedBlocks config
        map.put(PROTECTED_BLOCKS_KEY + "chest", "true");
        map.put(PROTECTED_BLOCKS_KEY + "door", "true");
        map.put(PROTECTED_BLOCKS_KEY + "shulker_box", "true");
        DEFAULT_CONFIG_DATA = Collections.unmodifiableMap(map);
    }

    /**
     * Config path.
     */
    private final Path configPath;
    /**
     * Config file description.
     */
    private final String configFileDescription;

    private Config() throws IOException {
        this(DEFAULT_CONFIG_PATH);
    }

    private Config(final Path configPath) throws IOException {
        this.configPath = configPath;
        this.configFileDescription = DEFAULT_CONFIG_FILE_DESCRIPTION;

        // Check if config dir exists
        if (!Files.exists(configPath.getParent())) {
            Files.createDirectories(configPath.getParent());
        }

        //Check if config file exists
        boolean fileSuccess = Files.exists(configPath);
        boolean created = false;
        if (!fileSuccess) {
            Files.createFile(configPath);
            // Initialize config file
            initConfig();
            created = true;
        }

        // Check and add missing fields if config file is not new
        if (!created) {
            completeConfig();
        }
    }

    /**
     * Get the config path.
     * @return the config path
     */
    public Path getConfigPath() {
        return configPath;
    }

    /**
     * Get the config properties.
     * @return Properties of the config
     * @throws IOException When cannot find the config file
     */
    private Properties getConfig() throws IOException {
        return FileResourcesUtils.readPropertiesFile(this.configPath.toString());
    }

    /**
     * Initialize instance of the config class.
     * @param configPath custom config path
     * @throws IOException When the file creation fail.
     */
    public static void initInstance(final Path configPath) throws IOException {
        instance = new Config(configPath);
    }

    /**
     * Get the current instance of the config class.
     * @return The config instance
     * @throws IOException
     */
    public static Config getInstance() throws IOException {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    /**
     * Get a key value from the config file.
     * @param key key to look for
     * @return string stored at this key.
     * @throws IOException When the key does not exist.
     */
    public String get(final String key) throws IOException {
        return this.getConfig().getProperty(key);
    }

    /**
     * Set data at a config key.
     * @param key Key to store data.
     * @param value Value to store.
     * @throws IOException
     */
    public void set(final String key, final String value) throws IOException {
        Properties props = this.getConfig();
        props.setProperty(key, value);
        saveConfig(props);
    }

    /**
     * Initialize config file with default params and save it.
     *
     * @throws IOException cannot save config file
     */
    private void initConfig() throws IOException {
        Properties props = this.getConfig();
        props.putAll(DEFAULT_CONFIG_DATA);
        saveConfig(props);
    }

    /**
     * Add missing params into config files and save it.
     *
     * @throws IOException cannot save config file
     */
    private void completeConfig() throws IOException {
        Properties props = this.getConfig();
        AtomicBoolean edited = new AtomicBoolean(false);
        DEFAULT_CONFIG_DATA.forEach((key, value) -> {
            // Check if key exists in config file
            if (!props.containsKey(key)) {
                props.put(key, value);
                edited.set(true);
            }
        });

        // Save config if edited
        if (edited.get()) {
            this.saveConfig(props);
        }
    }

    /**
     * Save config file.
     * @param toBeSaved List of properties to save.
     * @throws IOException cannot save config file
     */
    public void saveConfig(final Properties toBeSaved) throws IOException {
        try(FileOutputStream outputStream = new FileOutputStream(this.configPath.toString())) {
            toBeSaved.store(outputStream, configFileDescription);
        }
    }
}
