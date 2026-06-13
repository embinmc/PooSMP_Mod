package embin.poosmp.mixin;

import embin.poosmp.util.PooNameCache;
import net.minecraft.server.players.CachedUserNameToIdResolver;
import net.minecraft.server.players.NameAndId;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Map;

@Mixin(CachedUserNameToIdResolver.class)
public class CachedUserNameResolverMixin implements PooNameCache {
    @Shadow
    @Final
    private Map<String, CachedUserNameToIdResolver.GameProfileInfo> profilesByName;

    @Override
    public Collection<NameAndId> poosmp$getCachedNames() {
        return this.profilesByName.values().stream().map(CachedUserNameToIdResolver.GameProfileInfo::nameAndId).toList();
    }
}
