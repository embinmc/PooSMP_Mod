package embin.poosmp.mixin;

import com.mojang.authlib.GameProfile;
import embin.poosmp.util.PooPlayerList;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin implements PooPlayerList {
    @Shadow private @Final Map<UUID, ServerStatsCounter> stats;
    @Shadow private @Final MinecraftServer server;
    @Shadow private @Final PlayerDataStorage playerIo;

    @Shadow
    protected abstract Path locateStatsFile(GameProfile gameProfile);

    @Override
    public ServerStatsCounter poosmp$getPlayerStats(GameProfile gameProfile) {
        return this.stats.computeIfAbsent(gameProfile.id(), uuid -> {
            Path path = this.locateStatsFile(gameProfile);
            return new ServerStatsCounter(this.server, path);
        });
    }

    @Override
    public PlayerDataStorage poosmp$getPlayerDataStorage() {
        return this.playerIo;
    }
}
