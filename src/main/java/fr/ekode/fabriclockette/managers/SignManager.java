package fr.ekode.fabriclockette.managers;

import com.google.gson.JsonObject;
import fr.ekode.fabriclockette.api.ApiUser;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SignManager {

    /**
     * Constant for sign lines number.
     */
    private static final int SIGN_LINES_NUMBER = 3;

    /**
     * Entity of the current SignBlock.
     */
    private final SignBlockEntity sign;

    /**
     * Constructor of SignManager.
     *
     * @param currentSign sign to manage
     */
    public SignManager(final SignBlockEntity currentSign) {
        this.sign = currentSign;
    }

    /**
     * Return the Container where the sign is placed.
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
            if (blockState == null || !(blockState.getBlock() instanceof ProtectedBlock)) {
                // Check below block
                entityPos = entityPos.offset(Direction.Axis.Y, -1);
                blockState = world.getBlockState(entityPos);
            }

            if (blockState.getBlock() instanceof ProtectedBlock) {
                return new BlockStatePos(blockState, entityPos);
            }
        }
        return null;
    }

    /**
     * Check if this sign is private.
     *
     * @return true if private, false if not
     */
    public boolean isSignPrivate() {
        CompoundTag tag = new CompoundTag();
        tag = this.sign.toTag(tag);

        JsonObject json = JsonHelper.deserialize(tag.getString("Text1"));
        String text = json.get("text").getAsString();
        text = TextHelpers.removeMinecraftFormatingCodes(text);

        // Check if sign has [Private] or [More users] tag
        boolean privateTag = text.equals(PrivateTag.PRIVATE.getTagWithBrackets());
        boolean moreuserTag = text.equals(PrivateTag.MORE_USERS.getTagWithBrackets());
        boolean hasTag = (privateTag || moreuserTag);
        return hasTag;
    }

    /**
     * Get sign owners.
     *
     * @return list of UUID owners
     */
    public List<UUID> getSignOwners() {
        List<UUID> owners = new ArrayList<>();

        // Search each lines (2 to 4)
        for (int i = 0; i < SIGN_LINES_NUMBER; i++) {
            UUID owner = ((SignBlockEntityExt) sign).getOwner(i + 1);
            if (owner == null) {
                continue;
            }
            owners.add(owner);
        }

        return owners;
    }

    /**
     * Get sign usernames.
     *
     * @return list of usernames
     */
    public List<Text> getSignUsernames() {
        List<Text> usernames = new ArrayList<>();

        // Search each lines (2 to 4)
        for (int i = 0; i < SIGN_LINES_NUMBER; i++) {
            Text user = ((SignBlockEntityExt) sign).getTextOnRowServer(i + 1);
            if (user == null) {
                continue;
            }
            usernames.add(user);
        }

        return usernames;
    }

    /**
     * Check if this sign has owners.
     *
     * @return true if has owners, false if not
     */
    public boolean hasOwners() {
        return !this.getSignOwners().isEmpty();
    }

    /**
     * Remove every owners of the current sign.
     */
    public void removeSignOwners() {
        for (int i = 0; i < SIGN_LINES_NUMBER; i++) {
            ((SignBlockEntityExt) sign).setOwner(i + 1, null);
        }
    }

    /**
     * Add some formating (like [private] in bloc) for feedback.
     */
    public void formatSign() {
        if (this.isSignPrivate()) {
            ((SignBlockEntityExt) sign).setEditableServer(true);
            Text signText = ((SignBlockEntityExt) sign).getTextOnRowServer(0);


            if (signText.equals(new LiteralText(PrivateTag.PRIVATE.getTagWithBrackets()))) { // [Private]
                sign.setTextOnRow(0, new LiteralText("§l" + PrivateTag.PRIVATE.getTagWithBrackets()));
            } else if (signText.equals(new LiteralText(PrivateTag.MORE_USERS.getTagWithBrackets()))) { // [More Users]
                sign.setTextOnRow(0, new LiteralText("§l" + PrivateTag.MORE_USERS.getTagWithBrackets()));
            }
        }
    }

    /**
     * Add UUID for each username on sign.
     */
    public void populateSignUuids() {
        // Load Mojang online uuid retriever
        AuthHelper authHelper = AuthHelper.getInstance();

        // For each lines of sign
        for (int i = 0; i < SIGN_LINES_NUMBER; i++) {
            // reset owner
            ((SignBlockEntityExt) sign).setOwner(i + 1, null);

            Text username = ((SignBlockEntityExt) sign).getTextOnRowServer(i + 1);
            if (username.asString().equals("")) {
                continue; // Skip on empty line
            }

            username = TextHelpers.removeMinecraftFormatingCodes(username);
            String usernameS = username.asString();
            try {
                ApiUser profile = authHelper.getOnlineUUID(usernameS);

                // If user exist in userCache (Mojang Auth), set the text to italic and add owner
                if (profile == null) {
                    // Get offline player uuid and set the text to Strikethrough
                    UUID offlineUUid = PlayerEntity.getOfflinePlayerUuid(usernameS);
                    usernameS = "§o" + usernameS;
                    sign.setTextOnRow(i + 1, new LiteralText(usernameS));
                    ((SignBlockEntityExt) sign).setOwner(i + 1, offlineUUid);
                    continue;
                }
                usernameS = "§o" + usernameS;
                sign.setTextOnRow(i + 1, new LiteralText(usernameS));
                UUID userUuid = profile.getUUID();

                ((SignBlockEntityExt) sign).setOwner(i + 1, userUuid);
            } catch (IOException e) {
                e.printStackTrace();

                usernameS = "§m" + usernameS;
                sign.setTextOnRow(i + 1, new LiteralText(usernameS));
            }
        }
    }

    /**
     * Generate a sign with [private] and player name on first row.
     *
     * @param player player to add in first row
     * @param tag    tag of the sign (private, more users, etc...)
     */
    public void createDefaultSign(final PlayerEntity player, final PrivateTag tag) {
        this.sign.setTextOnRow(0, new LiteralText(tag.getTagWithBrackets()));

        Text username = player.getDisplayName();
        this.sign.setTextOnRow(1, username);

        this.populateSignUuids();
        this.formatSign();
    }

    /**
     * Check if a private sign can be placed.
     *
     * @param world        current world
     * @param containerPos position of the container
     * @param signPos      position of the sign
     * @param signFacing   facing direction of the sign
     * @return if the private sign can be placed here
     */
    public boolean canPlacePrivateSign(
            final World world,
            final BlockPos containerPos,
            final BlockPos signPos,
            final Direction signFacing) {

        Map<BlockPos, Direction> blockPosList
                = ProtectedBlockRepository.getAvailablePrivateSignPos(world, containerPos);
        if (blockPosList != null) {
            Direction facingPos = blockPosList.get(signPos);
            return signFacing.equals(facingPos);
        }
        return false;
    }
}
