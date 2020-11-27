package fr.ekode.FabricLockette.managers;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import fr.ekode.FabricLockette.core.TextHelpers;
import fr.ekode.FabricLockette.extentions.SignBlockEntityExt;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.io.File;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SignManager {
    private SignBlockEntity sign;

    public SignManager(SignBlockEntity sign) {
        this.sign = sign;
    }

    /**
     * Return the Container where the sign is placed
     *
     * @return LockableContainerBlockEntity container
     */
    public List<LockableContainerBlockEntity> getAttachedContainer() {
        World world = this.sign.getWorld();
        assert world != null;
        BlockState state = world.getBlockState(this.sign.getPos());

        // Check for a wall sign block
        if (state.getBlock() instanceof WallSignBlock) {
            //Get attached block
            Direction facing = state.get(WallSignBlock.FACING);
            BlockPos entityPos = sign.getPos().offset(facing, -1);

            BlockEntity entity = world.getBlockEntity(entityPos);
            BlockState blockState = world.getBlockState(entityPos);
            Block block = blockState.getBlock();

            if (entity instanceof LockableContainerBlockEntity) {
                ContainerManager containerManager = new ContainerManager((LockableContainerBlockEntity) entity);
                return containerManager.getContainers();
            }
        }
        return null;
    }

    public boolean isSignPrivate(){
        CompoundTag tag = new CompoundTag();
        tag = this.sign.toTag(tag);

        JsonObject json = JsonHelper.deserialize(tag.getString("Text1"));
        String text = json.get("text").getAsString();
        text = TextHelpers.removeMinecraftFormatingCodes(text);
        return sign != null && text.equals("[private]");
    }

    public List<UUID> getSignOwners(){
        List<UUID> owners = new ArrayList<>();

        // Search each lines (2 to 4)
        for(int i = 0;i<3;i++){
            UUID owner = ((SignBlockEntityExt) sign).getOwner(i+1);
            owners.add(owner);
        }

        return owners;
    }

    /**
     * Add some formating (like [private] in bloc) for feedback
     */
    public void formatSign(){
        if(this.isSignPrivate()){
            ((SignBlockEntityExt)sign).setEditable(true);
            sign.setTextOnRow(0,new LiteralText("§l[private]"));
        }
    }

    /**
     * Add UUID for each username on sign
     */
    public void populateSignUuids(){
        // Load Mojang online uuid retriever
        YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
        GameProfileRepository gameProfileRepository = yggdrasilAuthenticationService.createProfileRepository();
        UserCache userCache = new UserCache(gameProfileRepository, new File("", MinecraftServer.USER_CACHE_FILE.getName()));

        // For each lines of sign
        for(int i = 0; i<3; i++){
            Text username = ((SignBlockEntityExt )sign).getTextOnRow(i+1);
            if(username.asString().equals("")) continue;

            username = TextHelpers.removeMinecraftFormatingCodes(username);
            String usernameS = username.asString();
            GameProfile profile = userCache.findByName(usernameS);

            if(profile == null){
                usernameS = "§m"+usernameS;
                sign.setTextOnRow(i+1,new LiteralText(usernameS));
                ((SignBlockEntityExt) sign).setOwner(i+1,null);
                continue;
            }
            usernameS = "§o"+usernameS;
            sign.setTextOnRow(i+1,new LiteralText(usernameS));
            UUID userUuid = profile.getId();

            ((SignBlockEntityExt) sign).setOwner(i+1,userUuid);
        }
    }

    /**
     * Generate a sign with [private] and player name on first row
     */
    public void createDefaultSign(PlayerEntity player){
        this.sign.setTextOnRow(0,new LiteralText("[private]"));

        Text username = player.getDisplayName();
        this.sign.setTextOnRow(1,username);

        this.formatSign();
        this.populateSignUuids();
    }


}
