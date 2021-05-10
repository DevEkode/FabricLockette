package fr.ekode.fabriclockette.core;

import fr.ekode.fabriclockette.api.ApiUser;
import fr.ekode.fabriclockette.api.MojangService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class AuthHelper {

    /**
     * INSTANCE of the current AuthHelper
     */
    private static final AuthHelper INSTANCE = new AuthHelper();

    /** Service for Mojang API */
    private final MojangService service;

    private AuthHelper(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mojang.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(MojangService.class);

    }

    public static AuthHelper getInstance(){
        return INSTANCE;
    }

    public ApiUser getOnlineUUID(String username) throws IOException {
        Call<ApiUser> user = this.service.getUser(username);
        return user.execute().body();
    }
}
