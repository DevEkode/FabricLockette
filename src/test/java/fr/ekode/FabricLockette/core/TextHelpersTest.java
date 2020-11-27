package fr.ekode.FabricLockette.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextHelpersTest {

    String textWithCodes;
    String textWithoutCodes;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        textWithCodes = "§4RED TEXT §kUNREADABLE TEXT";
        textWithoutCodes = "RED TEXT UNREADABLE TEXT";
    }

    @Test
    void removeMinecraftFormatingCodes() {
        //TODO when fabric will support unit testing
        assertTrue(true);
    }
}