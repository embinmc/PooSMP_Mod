package embin.poosmp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import embin.poosmp.items.PooSMPItems;
import embin.poosmp.items.component.PooSMPItemComponents;
import embin.poosmp.items.component.ValueComponent;
import embin.poosmp.world.PooSMPSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public final class PooSMPCommands {
    private PooSMPCommands() {}

    static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess) {

        // sell current item in hand if possible
        dispatcher.register(Commands.literal("sellhand").executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            if (player == null) return 0;
            ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            MinecraftServer server = context.getSource().getServer();
            PooSMPSavedData savedData = PooSMPSavedData.get(server);
            Component cantSellText = Component.literal("Can't sell this item").withStyle(ChatFormatting.RED);
            if (itemStack.has(PooSMPItemComponents.ITEM_VALUE)) {
                ValueComponent value = itemStack.get(PooSMPItemComponents.ITEM_VALUE);
                if (value == null) return -2;
                if (value.canBeSold()) {
                    var profit = value.sellValue() * itemStack.getCount();
                    savedData.addBalance(player, profit);
                    var sellText = Component.literal(player.getPlainTextName() + " sold " + itemStack + " for $" + profit);
                    itemStack.setCount(0);
                    context.getSource().sendSuccess(() -> sellText, true);
                    return Command.SINGLE_SUCCESS;
                } else {
                    context.getSource().sendFailure(cantSellText);
                    return -3;
                }
            } else {
                context.getSource().sendFailure(cantSellText);
                return -1;
            }
        }));

        // tell player's balance
        dispatcher.register(Commands.literal("getbal").executes(context -> {
            MinecraftServer server = context.getSource().getServer();
            PooSMPSavedData savedData = PooSMPSavedData.get(server);
            if (savedData.balance.isEmpty()) {
                context.getSource().sendFailure(Component.literal("Everybody is broke"));
                return 0;
            }
            final int[] index = {1};
            savedData.balance.forEach((uuid, balance) -> {
                ServerPlayer player = server.getPlayerList().getPlayer(uuid);
                String formattedBalance = NumberFormat.getCurrencyInstance(Locale.US).format(balance);

                String playerName;
                if (player != null) {
                    playerName = player.getPlainTextName();
                } else if (savedData.getPlayerName(uuid).isPresent()) {
                    playerName = savedData.getPlayerName(uuid).orElseThrow();
                } else playerName = uuid.toString(); // fallback to player's uuid

                String message = index[0] + ": " + playerName + " > " + formattedBalance;
                context.getSource().sendSuccess(() -> Component.literal(message).withStyle(ChatFormatting.GOLD), false);
                index[0]++;
            });
            return Command.SINGLE_SUCCESS;
        }));

        // convert balance to money items
        dispatcher.register(Commands.literal("getmoney1").executes(context -> getMoneyItem(context.getSource(), PooSMPItems.ONE_DOLLAR_BILL)));
        dispatcher.register(Commands.literal("getmoney2").executes(context -> getMoneyItem(context.getSource(), PooSMPItems.TWO_DOLLAR_BILL)));
        dispatcher.register(Commands.literal("getmoney10").executes(context -> getMoneyItem(context.getSource(), PooSMPItems.TEN_DOLLAR_BILL)));
        dispatcher.register(Commands.literal("getmoney25").executes(context -> getMoneyItem(context.getSource(), PooSMPItems.TWENTY_FIVE_DOLLAR_BILL)));
        dispatcher.register(Commands.literal("getmoney50").executes(context -> getMoneyItem(context.getSource(), PooSMPItems.FIFTY_DOLLAR_BILL)));
        dispatcher.register(Commands.literal("getmoney100").executes(context -> getMoneyItem(context.getSource(), PooSMPItems.HUNDRED_DOLLAR_BILL)));

        // death count
        dispatcher.register(Commands.literal("deathcount").executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            return tellDeathCount(context, player);
        }).then(Commands.argument("target", EntityArgument.player()).executes(context -> {
            ServerPlayer player = EntityArgument.getPlayer(context, "target");
            return tellDeathCount(context, player);
        })));

        // public /tellraw command
        dispatcher.register(Commands.literal("tellraw2")
                // target argument only allows single player names when not op status, so we just won't have it and broadcast to everyone
            .then(Commands.argument("message", ComponentArgument.textComponent(registryAccess))
            .executes(commandContext -> {
                List<ServerPlayer> players = commandContext.getSource().getServer().getPlayerList().getPlayers();

                for (ServerPlayer serverPlayer : players) {
                    serverPlayer.sendSystemMessage(ComponentArgument.getResolvedComponent(commandContext, "message", serverPlayer), false);
                }

                return players.size();
            })
        ));
    }

    @SuppressWarnings("DataFlowIssue")
    private static int getMoneyItem(CommandSourceStack context, Item moneyItem) {
        ItemStack moneyStack = moneyItem.getDefaultInstance();
        if (moneyStack.has(PooSMPItemComponents.MONEY)) {
            final double moneyAmount = moneyStack.get(PooSMPItemComponents.MONEY);
            MinecraftServer server = context.getServer();
            ServerPlayer player = context.getPlayer();
            if (player == null) return 0;
            PooSMPSavedData savedData = PooSMPSavedData.get(server);
            final double playerBalance = savedData.getBalance(player);
            if (moneyAmount > playerBalance) {
                context.sendFailure(Component.literal("You are too broke to get this"));
                return -1;
            }
            savedData.addBalance(player, -moneyAmount);
            ServerLevel level = player.level();
            level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), moneyStack.copy()));
            return Command.SINGLE_SUCCESS;
        } else return -50;
    }

    private static int tellDeathCount(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        if (player == null) return 0;
        int deaths = player.getStats().getValue(Stats.CUSTOM.get(Stats.DEATHS));
        context.getSource().sendSuccess(() -> Component.literal(player.getPlainTextName() + " has died " + deaths + " time(s)."), false);
        return deaths;
    }
}
