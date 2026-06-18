package embin.poosmp.items;

import embin.poosmp.items.component.PooSMPItemComponents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class CreativeSnitchItem extends Item {
    public static final List<String> KNOWN_OPERATORS = List.of("_thecubic_", "ddededodediamant", "Embin", "whentheapple");

    public CreativeSnitchItem(Properties settings, boolean snitch) {
        super(settings.component(PooSMPItemComponents.FROM_CREATIVE, snitch));
    }
    public CreativeSnitchItem(Properties settings) {
        super(settings.component(PooSMPItemComponents.FROM_CREATIVE, true));
    }


    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel serverLevel, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
        super.inventoryTick(itemStack, serverLevel, entity, equipmentSlot);
        CreativeSnitchItem.invTick(itemStack, serverLevel, entity);
    }

    public static void invTick(ItemStack itemStack, ServerLevel serverLevel, Entity entity) {
        if (!serverLevel.isClientSide() && entity.isAlwaysTicking()) {
            if (itemStack.has(PooSMPItemComponents.FROM_CREATIVE)) {
                if (itemStack.getOrDefault(PooSMPItemComponents.FROM_CREATIVE, false)) {
                    MinecraftServer server = serverLevel.getServer();
                    CommandSourceStack source = server.createCommandSourceStack().withSuppressedOutput();
                    Commands commandManager = server.getCommands();
                    String player_name = entity.getPlainTextName();
                    Component message = Component.literal(player_name).append(" took a ").append(itemStack.getStyledHoverName()).append(" from Creative Mode or /give");
                    if (CreativeSnitchItem.KNOWN_OPERATORS.contains(player_name) || player_name.startsWith("Player")) {
                        commandManager.performPrefixedCommand(source, "tellraw @a \"" + message.getString() + "\"");
                    }
                }
                itemStack.set(PooSMPItemComponents.FROM_CREATIVE, null);
            }
        }
    }
}