package embin.poosmp.mixin.client;

import com.tiviacz.travelersbackpack.client.screens.HudOverlay;
import com.tiviacz.travelersbackpack.config.TravelersBackpackConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HudOverlay.class)
public class BackpackHudMixin {
    @Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!TravelersBackpackConfig.clientSpec.isLoaded() || !TravelersBackpackConfig.serverSpec.isLoaded()) {
            ci.cancel();
            return;
        }
        if (Minecraft.getInstance().player == null)
            ci.cancel();
    }
}
