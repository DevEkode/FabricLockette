package fr.ekode.fabriclockette.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MojangService {
    /**
     * Call to the users/profiles/minecraft/{user} MojangAPI GET endpoint
     * @param username Minecraft username
     * @return Call promise of ApiUser
     */
    @GET("users/profiles/minecraft/{user}")
    Call<ApiUser> getUser(@Path("user") String username);
}
