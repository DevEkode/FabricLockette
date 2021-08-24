package fr.ekode.fabriclockette.entities;

import java.util.UUID;

public class SignOwner {
    /**
     * UUID Of the player owner.
     */
    private UUID uuid;
    /**
     * Username of the player owner.
     */
    private String username;

    /**
     * Constructor of the SignOwner class.
     * @param uuid owner uuid
     * @param username owner username
     */
    public SignOwner(final UUID uuid, final String username) {
        this.uuid = uuid;
        this.username = username;
    }

    /**
     * Get the player UUID.
     * @return player UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Set the player UUID.
     * @param uuid player UUID
     */
    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the player username.
     * @return player username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the player username.
     * @param username player username
     */
    public void setUsername(final String username) {
        this.username = username;
    }
}
