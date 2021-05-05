package fr.ekode.fabriclockette.core;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import fr.ekode.fabriclockette.api.ApiUser;
import fr.ekode.fabriclockette.api.MojangService;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.UserCache;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.UUID;

public class AuthHelper {

    private static final AuthHelper INSTANCE = new AuthHelper();

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
