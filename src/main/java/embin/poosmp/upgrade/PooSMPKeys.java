package embin.poosmp.upgrade;

import embin.poosmp.world.PooSMPRegistries;
import embin.poosmp.util.Id;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class PooSMPKeys {
    public static final ResourceKey<Upgrade> HEALTH_INCREASE = upgrade("health_increase");
    public static final ResourceKey<Level> HYRULE = dimension("hyrule");
    public static final ResourceKey<Level> MISSINGNO = dimension("missingno");
    public static final ResourceKey<Level> DIMWORLD = dimension("dimworld");

    public static ResourceKey<Upgrade> upgrade(String id) {
        return ResourceKey.create(PooSMPRegistries.Keys.UPGRADE, Id.of(id));
    }

    public static ResourceKey<Level> dimension(String id) {
        return ResourceKey.create(Registries.DIMENSION, Id.of(id));
    }
}
