package fr.ekode.fabriclockette.managers;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import fr.ekode.fabriclockette.blocks.ProtectedBlock;
import fr.ekode.fabriclockette.blocks.ProtectedBlockRepository;
import fr.ekode.fabriclockette.core.AuthHelper;
import fr.ekode.fabriclockette.core.TextHelpers;
import fr.ekode.fabriclockette.entities.BlockStatePos;
import fr.ekode.fabriclockette.enums.PrivateTag;
import fr.ekode.fabriclockette.extentions.SignBlockEntityExt;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.UserCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public BlockStatePos getAttachedContainer() {
        World world = this.sign.getWorld();
        assert world != null;
        BlockState state = world.getBlockState(this.sign.getPos());

        // Check for a wall sign block
        if (state.getBlock() instanceof WallSignBlock) {
            //Get attached block
            Direction facing = state.get(WallSignBlock.FACING);
            // Check behind block
            BlockPos entityPos = sign.getPos().offset(facing, -1);
            BlockState blockState = world.getBlockState(entityPos);
            if(blockState == null || !(blockState.getBlock() instanceof ProtectedBlock)){
                // Check below block
                entityPos = entityPos.offset(Direction.Axis.Y,-1);
                blockState = world.getBlockState(entityPos);
            }

            if(blockState.getBlock() instanceof ProtectedBlock) return new BlockStatePos(blockState,entityPos);
        }
        return null;
    }

    public boolean isSignPrivate(){
        CompoundTag tag = new CompoundTag();
        tag = this.sign.toTag(tag);

        JsonObject json = JsonHelper.deserialize(tag.getString("Text1"));
        String text = json.get("text").getAsString();
        text = TextHelpers.removeMinecraftFormatingCodes(text);

        // Check if sign has [Private] or [More users] tag
        boolean hasTag = text.equals(PrivateTag.PRIVATE.getTagWithBrackets()) || text.equals(PrivateTag.MORE_USERS.getTagWithBrackets());
        // Check if sign has at least one name
        boolean hasOwners = !getSignOwners().isEmpty();
        return hasTag && hasOwners;
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
            ((SignBlockEntityExt)sign).setEditableServer(true);
            Text signText = ((SignBlockEntityExt)sign).getTextOnRowServer(0);

            // [Private]
            if(signText.equals(new LiteralText(PrivateTag.PRIVATE.getTagWithBrackets()))){
                sign.setTextOnRow(0,new LiteralText("§l"+PrivateTag.PRIVATE.getTagWithBrackets()));
            }
            // [More Users]
            else if(signText.equals(new LiteralText(PrivateTag.MORE_USERS.getTagWithBrackets()))){
                sign.setTextOnRow(0,new LiteralText("§l"+PrivateTag.MORE_USERS.getTagWithBrackets()));
            }
        }
    }

    /**
     * Add UUID for each username on sign
     */
    public void populateSignUuids(){
        // Load Mojang online uuid retriever
        UserCache userCache = AuthHelper.getInstance().getUserCache();

        // For each lines of sign
        for(int i = 0; i<3; i++){
            // reset owner
            ((SignBlockEntityExt) sign).setOwner(i+1,null);

            Text username = ((SignBlockEntityExt )sign).getTextOnRowServer(i+1);
            if(username.asString().equals("")) continue; // Skip on empty line

            username = TextHelpers.removeMinecraftFormatingCodes(username);
            String usernameS = username.asString();
            GameProfile profile = userCache.findByName(usernameS);

            // If user exist in userCache (Mojang Auth), set the text to italic and add owner
            if(profile == null){
                // Get offline player uuid and set the text to Strikethrough
                UUID offlineUUid = PlayerEntity.getOfflinePlayerUuid(usernameS);
                usernameS = "§m"+usernameS;
                sign.setTextOnRow(i+1,new LiteralText(usernameS));
                ((SignBlockEntityExt) sign).setOwner(i+1,offlineUUid);
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
     * @param player player to add in first row
     * @param tag tag of the sign (private, more users, etc...)
     */
    public void createDefaultSign(PlayerEntity player,PrivateTag tag){
        this.sign.setTextOnRow(0,new LiteralText(tag.getTagWithBrackets()));

        Text username = player.getDisplayName();
        this.sign.setTextOnRow(1,username);

        this.populateSignUuids();
        this.formatSign();
    }

    /**
     * Check if a private sign can be placed
     * @param world current world
     * @param containerPos position of the container
     * @param signPos position of the sign
     * @param signFacing facing direction of the sign
     * @return if the private sign can be placed here
     */
    public boolean canPlacePrivateSign(World world, BlockPos containerPos, BlockPos signPos, Direction signFacing){
        Map<BlockPos, Direction> blockPosList = ProtectedBlockRepository.getAvailablePrivateSignPos(world,containerPos);
        if(blockPosList != null){
            Direction facingPos = blockPosList.get(signPos);
            return signFacing.equals(facingPos);
        }
        return false;
    }
}
