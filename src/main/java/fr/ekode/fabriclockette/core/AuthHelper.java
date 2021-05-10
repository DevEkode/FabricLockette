package fr.ekode.fabriclockette.core;

import fr.ekode.fabriclockette.api.ApiUser;
import fr.ekode.fabriclockette.api.MojangService;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public final class AuthHelper {

    /**
     * INSTANCE of the current AuthHelper.
     */
    private static final AuthHelper INSTANCE = new AuthHelper();

    /**
     * Service for Mojang API.
     */
    private final MojangService service;

    private AuthHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mojang.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(MojangService.class);

    }

    /**
     * Get current AuthHelper Instance.
     * @return AuthHelper Instance
     */
    public static AuthHelper getInstance() {
        return INSTANCE;
    }

    /**
     * Get Minecraft player UUID with his username using Mojang API.
     * @param username Minecraft username
     * @return ApiUser reponse from API
     * @throws IOException
     */
    public ApiUser getOnlineUUID(final String username) throws IOException {
        Call<ApiUser> user = this.service.getUser(username);
        return user.execute().body();
    }
}

