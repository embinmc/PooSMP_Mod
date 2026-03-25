package embin.poosmp.items;

import embin.poosmp.items.component.PooSMPItemComponents;
import embin.poosmp.util.Id;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.level.Level;

public final class ItemUses {
    public static void register() {
        UseItemCallback.EVENT.register(Id.of("jumpscare_stick"), ItemUses::jumpscareStick);
        UseItemCallback.EVENT.register(Id.of("fun_stick"), ItemUses::funStick);
    }

    private static InteractionResult jumpscareStick(Player player, Level level, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.has(PooSMPItemComponents.JUMPSCARE_STICK)) {
            if (player.getCooldowns().isOnCooldown(itemStack)) return InteractionResult.PASS;
            if (level instanceof ServerLevel serverLevel) {
                for (ServerPlayer serverPlayer : serverLevel.getServer().getPlayerList().getPlayers()) {
                    serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT, 1f));
                }
            }
            if (itemStack.has(DataComponents.USE_COOLDOWN)) {
                UseCooldown useCooldown = itemStack.get(DataComponents.USE_COOLDOWN);
                useCooldown.apply(itemStack, player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult funStick(Player player, Level level, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.has(PooSMPItemComponents.FUN_STICK)) {
            if (player.getCooldowns().isOnCooldown(itemStack)) return InteractionResult.PASS;
            final int chosenEvent = player.getRandom().nextIntBetweenInclusive(0, 2);
            switch (chosenEvent) {
                case 0 -> { // jumpscare
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT, 1f));
                    }
                }
                case 1 -> { // demo
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 0f));
                    }
                }
                case 2 -> { // screen
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundOpenScreenPacket(0, MenuType.GENERIC_9x5, Component.literal("gay")));
                    }
                }
            }
            if (itemStack.has(DataComponents.USE_COOLDOWN)) {
                UseCooldown useCooldown = itemStack.get(DataComponents.USE_COOLDOWN);
                useCooldown.apply(itemStack, player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
