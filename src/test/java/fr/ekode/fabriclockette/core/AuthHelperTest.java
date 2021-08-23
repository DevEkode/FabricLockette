package fr.ekode.fabriclockette.core;

import fr.ekode.fabriclockette.api.ApiUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthHelperTest {

    private AuthHelper authHelper;
    private String username;
    private UUID userUUID;

    @BeforeEach
    void setUp() {
        authHelper = AuthHelper.getInstance();
        username = "Ekode";
        userUUID = UUID.fromString("4c9baf40-6755-4bdb-b924-64e4363df9ee");
    }

    @Test
    void getOnlineUUID() throws IOException {
        ApiUser user = this.authHelper.getOnlineUUID(this.username);
        assertEquals(user.getUUID(),this.userUUID);
    }

    @Test
    void getOnlineUUIDName() throws IOException {
        ApiUser user = this.authHelper.getOnlineUUID(this.username);
        assertEquals(user.getName(),this.username);
    }
}