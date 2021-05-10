package fr.ekode.fabriclockette.api;

import javax.annotation.Generated;

import com.github.f4b6a3.uuid.codec.UuidCodec;
import com.github.f4b6a3.uuid.codec.base.Base16Codec;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

@Generated("jsonschema2pojo")
public class ApiUser {

    /**
     * Minecraft username from the API response
     */
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * String UUID from the API response
     */
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * Getter of name
     * @return Minecraft username
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of id by converting into Java UUID with uuid-creator
     * @return Minecraft user UUID
     */
    public UUID getUUID() {
        UuidCodec<String> codec = new Base16Codec();
        return codec.decode(this.id);
    }
}
