package embin.poosmp.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.level.storage.PlayerDataStorage;

public interface PooPlayerList {
    ServerStatsCounter poosmp$getPlayerStats(final GameProfile gameProfile);
    PlayerDataStorage poosmp$getPlayerDataStorage();
}
