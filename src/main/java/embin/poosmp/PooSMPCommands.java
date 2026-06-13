package embin.poosmp;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import embin.poosmp.items.PooSMPItems;
import embin.poosmp.items.component.PooSMPItemComponents;
import embin.poosmp.items.component.ValueComponent;
import embin.poosmp.util.PooNameCache;
import embin.poosmp.util.PooPlayerList;
import embin.poosmp.util.PooUtil;
import embin.poosmp.world.PooSMPSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.text.NumberFormat;
import java.util.*;

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
            var nf = NumberFormat.getCurrencyInstance(Locale.US);
            final int[] index = {1};
            final double[] totalMoney = {0D};
            savedData.balance.values().forEach(d -> totalMoney[0] += d);
            if (context.getSource().getPlayer() != null)
                context.getSource().getPlayer().sendSystemMessage(Component.literal("Total money: " + nf.format(totalMoney[0])).withStyle(ChatFormatting.GOLD));
            savedData.balance.keySet().stream().sorted(Comparator.comparingDouble(k -> savedData.balance.get(k)).reversed()).forEach(uuid -> {
                double balance = savedData.balance.get(uuid);
                ServerPlayer player = server.getPlayerList().getPlayer(uuid);
                String formattedBalance = nf.format(balance);

                String playerName;
                if (player != null) {
                    playerName = player.getPlainTextName();
                } else if (savedData.getPlayerName(uuid).isPresent()) {
                    playerName = savedData.getPlayerName(uuid).orElseThrow();
                } else playerName = uuid.toString(); // fallback to player's uuid

                String message = index[0] + ": " + playerName + " -> " + formattedBalance;
                if (context.getSource().getPlayer() != null)
                    context.getSource().getPlayer().sendSystemMessage(Component.literal(message).withStyle(ChatFormatting.GOLD));
                index[0]++;
            });
            return Command.SINGLE_SUCCESS;
        }));

        // convert balance to money items
        registerMoneyCommand(dispatcher, "getmoney1", PooSMPItems.ONE_DOLLAR_BILL);
        registerMoneyCommand(dispatcher, "getmoney2", PooSMPItems.TWO_DOLLAR_BILL);
        registerMoneyCommand(dispatcher, "getmoney5", PooSMPItems.FIVE_DOLLAR_BILL);
        registerMoneyCommand(dispatcher, "getmoney10", PooSMPItems.TEN_DOLLAR_BILL);
        registerMoneyCommand(dispatcher, "getmoney25", PooSMPItems.TWENTY_FIVE_DOLLAR_BILL);
        registerMoneyCommand(dispatcher, "getmoney50", PooSMPItems.FIFTY_DOLLAR_BILL);
        registerMoneyCommand(dispatcher, "getmoney100", PooSMPItems.HUNDRED_DOLLAR_BILL);

        // death count
        dispatcher.register(Commands.literal("deathcount").executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            if (player == null)
                return -5;
            int deaths = getDeathCount(context, player.nameAndId());
            player.sendSystemMessage(Component.literal("You have died " + deaths + " time(s)."));
            return deaths;
        }).then(Commands.literal("list").executes(context -> {
            ServerPlayer asker = context.getSource().getPlayer();
            if (asker == null)
                return -5;
            MinecraftServer server = context.getSource().getServer();
            if (!(server.services().nameToIdCache() instanceof PooNameCache pooNameCache)) {
                context.getSource().sendFailure(Component.literal("Couldn't fetch death counts").withStyle(ChatFormatting.RED));
                return -10;
            }
            Collection<NameAndId> cache = pooNameCache.poosmp$getCachedNames();
            Map<NameAndId, Integer> deathCounts = HashMap.newHashMap(cache.size());
            int totalDeaths = 0;
            for (NameAndId player : cache) {
                int result = getDeathCount(context, player);
                if (result < 0)
                    continue;
                deathCounts.put(player, result);
                totalDeaths += result;
            }
            final int[] index = {1};
            asker.sendSystemMessage(Component.literal("Total deaths: " + totalDeaths));
            deathCounts.keySet().stream().sorted(Comparator.comparingInt(deathCounts::get)).forEach(nameAndId -> {
                int deaths = deathCounts.get(nameAndId);
                Component text = Component.literal(index[0] + ": " + nameAndId.name() + " -> " + deaths);
                asker.sendSystemMessage(text);
                index[0]++;
            });
            return totalDeaths;
        })));

        // public /tellraw command
        dispatcher.register(Commands.literal("tellraw2")
                // target argument only allows single player names when not op status, so we just won't have it and broadcast to everyone
            .then(Commands.argument("message", ComponentArgument.textComponent(registryAccess))
            .executes(commandContext -> {
                MinecraftServer server = commandContext.getSource().getServer();
                List<ServerPlayer> players = server.getPlayerList().getPlayers();
                for (ServerPlayer player : players) {
                    player.sendSystemMessage(ComponentArgument.getResolvedComponent(commandContext, "message", player));
                }
                return players.size();
            })
        ));

        dispatcher.register(Commands.literal("suicide").executes(context -> {
            ServerPlayer player = context.getSource().getPlayerOrException();
            ServerLevel level = context.getSource().getLevel();
            player.sendSystemMessage(Component.literal("Attempting to KILL YOU!!!!!"));
            player.hurtServer(context.getSource().getLevel(), level.damageSources().source(PooSMPMod.SUICIDE), Float.MAX_VALUE);
            return 1;
        }));

        dispatcher.register(Commands.literal("randomuuid").executes(context -> {
            List<Entity> entities = PooUtil.listFromEnumeration(context.getSource().getLevel().getAllEntities().iterator());
            int index = context.getSource().getLevel().getRandom().nextInt(0, entities.size());
            try {
                Entity entity = entities.get(index);
                context.getSource().getPlayerOrException().sendSystemMessage(Component.literal(entity.toString()));
                context.getSource().getPlayerOrException().sendSystemMessage(ComponentUtils.copyOnClickText(entity.getStringUUID()));
                return index;
            } catch (IndexOutOfBoundsException e) {
                context.getSource().getPlayerOrException().sendSystemMessage(Component.literal("failed").withStyle(ChatFormatting.RED));
                return -1;
            }
        }));
    }

    public static void registerMoneyCommand(CommandDispatcher<CommandSourceStack> dispatcher, String name, Item moneyItem) {
        var com = Commands.literal(name).executes(c -> getMoneyItem(c.getSource(), moneyItem));
        dispatcher.register(com.then(Commands.argument("amount", IntegerArgumentType.integer(1, 99)).executes(context -> {
            final int amount = IntegerArgumentType.getInteger(context, "amount");
            int remaining = amount;
            while (remaining > 0) {
                int result = getMoneyItem(context.getSource(), moneyItem);
                remaining--;
                if (result < 1)
                    return result;
            }
            return amount;
        })));
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

    private static int getDeathCount(CommandContext<CommandSourceStack> context, NameAndId player) {
        if (player == null) return 0;
        PlayerList playerList = context.getSource().getServer().getPlayerList();
        if (!(playerList instanceof PooPlayerList pooPlayerList)) {
            context.getSource().sendFailure(Component.literal("Failed to get stats for " + player.name()).withStyle(ChatFormatting.RED));
            return -10;
        }
        boolean isRealPlayer = playerList.getPlayer(player.name()) != null || pooPlayerList.poosmp$getPlayerDataStorage().load(player).isPresent();
        if (!isRealPlayer)
            return -1;
        //PooSMPMod.LOGGER.info("Trying to get stats for player {} ({})", player.name(), player.id());
        ServerStatsCounter stats = pooPlayerList.poosmp$getPlayerStats(new GameProfile(player.id(), player.name()));
        return stats.getValue(Stats.CUSTOM.get(Stats.DEATHS));
    }
}
