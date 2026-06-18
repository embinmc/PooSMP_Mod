package embin.poosmp.mixin;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import com.tiviacz.travelersbackpack.config.TravelersBackpackConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TravelersBackpack.class)
public abstract class BackpackMixin {

    @Shadow
    public static boolean accessoriesLoaded;

    @Shadow
    public static boolean trinketsLoaded;

    @Overwrite
    public static boolean enableAccessories() {
        if (!accessoriesLoaded || !TravelersBackpackConfig.serverSpec.isLoaded())
            return false;
        if (TravelersBackpackConfig.SERVER == null || TravelersBackpackConfig.SERVER.backpackSettings == null)
            return false;
        if (TravelersBackpackConfig.SERVER.backpackSettings.backSlotIntegration == null)
            return false;
        return TravelersBackpackConfig.SERVER.backpackSettings.backSlotIntegration.getAsBoolean();
    }

    @Overwrite
    public static boolean enableTrinkets() {
        if (!trinketsLoaded || enableAccessories() || !TravelersBackpackConfig.serverSpec.isLoaded())
            return false;
        if (TravelersBackpackConfig.SERVER == null || TravelersBackpackConfig.SERVER.backpackSettings == null)
            return false;
        if (TravelersBackpackConfig.SERVER.backpackSettings.backSlotIntegration == null)
            return false;
        return TravelersBackpackConfig.SERVER.backpackSettings.backSlotIntegration.getAsBoolean();
    }
}
