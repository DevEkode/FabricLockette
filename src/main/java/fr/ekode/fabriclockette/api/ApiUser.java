package fr.ekode.fabriclockette.api;

import javax.annotation.Generated;

import com.github.f4b6a3.uuid.codec.UuidCodec;
import com.github.f4b6a3.uuid.codec.base.Base16Codec;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

@Generated("jsonschema2pojo")
public class ApiUser {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        UuidCodec<String> codec = new Base16Codec();
        return codec.decode(this.id);
    }

}