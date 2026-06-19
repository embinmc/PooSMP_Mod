package embin.poosmp.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.tiviacz.travelersbackpack.inventory.menu.slot.BackpackSlotItemHandler;
import embin.poosmp.items.component.PooSMPItemComponents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BackpackSlotItemHandler.class)
public class BackpackSlotMixin {
    @ModifyReturnValue(method = "isItemValid", at = @At("RETURN"))
    private static boolean allowRecursive(boolean original, @Local(argsOnly = true) ItemStack itemStack) {
        //if (itemStack.has(PooSMPItemComponents.FORCE_ALLOW_IN_BACKPACK))
        //    return true;
        return original;
    }
}
