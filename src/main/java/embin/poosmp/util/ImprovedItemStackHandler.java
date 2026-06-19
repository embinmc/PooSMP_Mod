package embin.poosmp.util;

import com.tiviacz.travelersbackpack.init.ModDataComponents;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.handler.ItemStackHandler;
import com.tiviacz.travelersbackpack.inventory.menu.slot.BackpackSlotItemHandler;
import com.tiviacz.travelersbackpack.inventory.menu.slot.ToolSlotItemHandler;
import embin.poosmp.items.component.PooSMPItemComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class ImprovedItemStackHandler extends ItemStackHandler {
    protected final BackpackWrapper backpackWrapper;
    protected final int dataId;

    public ImprovedItemStackHandler(BackpackWrapper backpackWrapper, NonNullList<ItemStack> stacks, int dataId) {
        super(stacks);
        this.backpackWrapper = backpackWrapper;
        this.dataId = dataId;
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.backpackWrapper.setSlotChanged(slot, this.getStackInSlot(slot), dataId);
        if (this.dataId == 2) {
            this.backpackWrapper.sendDataToClients(ModDataComponents.TOOLS_CONTAINER);
        }

        this.backpackWrapper.saveHandler.run();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (this.dataId == 2)
            return ToolSlotItemHandler.isValid(stack);
        if (this.backpackWrapper.getBackpackStack().has(PooSMPItemComponents.FORCE_ALLOW_IN_BACKPACK))
            return !stack.has(PooSMPItemComponents.FORCE_ALLOW_IN_BACKPACK);
        return BackpackSlotItemHandler.isItemValid(stack);
    }
}
