package fr.ekode.fabriclockette.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {
    @TempDir
    static Path sharedTempDir;

    private Config config;

    @BeforeEach
    void setUp() throws IOException {
        Path configPath = sharedTempDir.resolve("config.properties");
        Config.initInstance(configPath);
        this.config = Config.getInstance();
    }

    @Test
    void checkFileCreation(){
        assertTrue(Files.exists(config.getConfigPath()));
    }

    @Test
    void checkDirectoryCreation(){
        assertTrue(Files.exists(config.getConfigPath().getParent()));
    }

    @Test
    void get() throws IOException {
        String configData = this.config.get("lang");
        assertNotNull(configData);
    }

    @Test
    void set() throws IOException {
        String expected = "this_is_a_test";
        this.config.set("testing",expected);

        String configData = this.config.get("testing");
        assertEquals(expected,configData);
    }
}