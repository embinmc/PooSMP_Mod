package embin.poosmp.mixin;

import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.handler.ItemStackHandler;
import embin.poosmp.util.ImprovedItemStackHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BackpackWrapper.class)
public class BackpackWrapperMixin {

    /**
     * @author Embin
     * @reason Can't inject normally
     */
    @Overwrite
    private ItemStackHandler createHandler(NonNullList<ItemStack> stacks, final int dataId) {
        BackpackWrapper myself = (BackpackWrapper)(Object)this;
        return new ImprovedItemStackHandler(myself, stacks, dataId);
    }
}
