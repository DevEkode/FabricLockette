package fr.ekode.fabriclockette.core;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.UserCache;

import java.io.File;
import java.net.Proxy;
import java.util.UUID;

public class AuthHelper {

    private static final AuthHelper INSTANCE = new AuthHelper();
    private final UserCache userCache;

    private AuthHelper(){
        YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
        GameProfileRepository gameProfileRepository = yggdrasilAuthenticationService.createProfileRepository();
        userCache = new UserCache(gameProfileRepository, new File("", MinecraftServer.USER_CACHE_FILE.getName()));
    }

    public static AuthHelper getInstance(){
        return INSTANCE;
    }

    public UserCache getUserCache(){
        return this.userCache;
    }
}
