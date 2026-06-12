package embin.poosmp.util;

import embin.poosmp.block.PooSMPBlocks;
import embin.poosmp.items.PooSMPItems;
import embin.poosmp.villager.PooSMPVillagers;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class TradeConstructors {

    public static void register_villager_trades() {
        TradeOfferHelper.registerVillagerOffers(PooSMPVillagers.BANKER_KEY, 1, factories -> {
            factories.add(trade(Items.SAND, 10, PooSMPItems.ONE_DOLLAR_BILL));
            factories.add(trade(Items.GRASS_BLOCK, 10, PooSMPItems.ONE_DOLLAR_BILL));
            factories.add(trade(Items.LAPIS_LAZULI, 1, PooSMPItems.ONE_DOLLAR_BILL));
            factories.add(trade(Items.SUGAR_CANE, 10, PooSMPItems.ONE_DOLLAR_BILL));
        });
        TradeOfferHelper.registerVillagerOffers(PooSMPVillagers.BANKER_KEY, 2, factories -> {
            factories.add(trade(Items.RED_SAND, 4, PooSMPItems.ONE_DOLLAR_BILL));
            factories.add(trade(Items.IRON_INGOT, 2, PooSMPItems.ONE_DOLLAR_BILL, 9));
            factories.add(trade(Items.EMERALD, 1, PooSMPItems.ONE_DOLLAR_BILL, 8));
            factories.add(trade(Items.APPLE, 1, PooSMPItems.ONE_DOLLAR_BILL));
        });
        TradeOfferHelper.registerVillagerOffers(PooSMPVillagers.BANKER_KEY, 3, factories -> {
            factories.add(trade(Items.GOLD_INGOT, 2, PooSMPItems.ONE_DOLLAR_BILL, 17));
            factories.add(trade(Items.DIAMOND, 1, PooSMPItems.ONE_DOLLAR_BILL, 18));
            factories.add(trade(Items.ECHO_SHARD, 1, PooSMPItems.ONE_DOLLAR_BILL, 64));
        });
        TradeOfferHelper.registerVillagerOffers(PooSMPVillagers.BANKER_KEY, 4, factories -> {
            factories.add(trade(Items.NETHERITE_SCRAP, 1, PooSMPItems.HUNDRED_DOLLAR_BILL, 4));
            factories.add(enchantedBook(PooSMPTags.Enchantments.BANKER_TRADEABLE, PooSMPItems.HUNDRED_DOLLAR_BILL, 3));
            factories.add(trade(PooSMPBlocks.DRAGON_ANNOYANCE, 8, PooSMPItems.FIVE_DOLLAR_BILL, 3));
        });
        TradeOfferHelper.registerVillagerOffers(PooSMPVillagers.BANKER_KEY, 5, factories -> {
            factories.add(trade(Items.ELYTRA, 1, PooSMPItems.HUNDRED_DOLLAR_BILL, 40));
            factories.add(trade(PooSMPItems.RAW_RED_POO, 1, PooSMPItems.HUNDRED_DOLLAR_BILL, 64));
            factories.add(trade(PooSMPItems.RED_POO_UPGRADE_SMITHING_TEMPLATE, 1, PooSMPItems.HUNDRED_DOLLAR_BILL, 10));
            factories.add(trade(PooSMPBlocks.DDEDEDODEDIAMANTE_BLOCK, 16, PooSMPItems.TEN_DOLLAR_BILL, 2));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 5, factories -> {
            factories.add(trade(Items.SHULKER_SHELL, 2, PooSMPItems.HUNDRED_DOLLAR_BILL, 1));
            factories.add(trade(PooSMPItems.BANANA, 4, Items.EMERALD, 2));
        });
    }

    public static VillagerTrades.ItemListing trade(ItemStack buying, Item cost, int costAmount) {
        return (serverLevel, entity, randomSource) -> new ToMoney(buying, cost, costAmount).getOffer(serverLevel, entity, randomSource);
    }

    public static VillagerTrades.ItemListing trade(ItemStackSupplier buying, Item cost, int costAmount) {
        return (serverLevel, entity, randomSource) -> new ToMoney(buying.get(serverLevel, entity, randomSource), cost, costAmount).getOffer(serverLevel, entity, randomSource);
    }

    public static VillagerTrades.ItemListing trade(ItemLike buying, int buyingAmount, Item cost, int costAmount) {
        return trade(new ItemStack(buying, buyingAmount), cost, costAmount);
    }

    public static VillagerTrades.ItemListing trade(ItemLike buying, int buyingAmount, Item cost) {
        return trade(buying, buyingAmount, cost, 1);
    }

    public static VillagerTrades.ItemListing enchantedBook(TagKey<Enchantment> tag, ItemLike cost, int costAmount) {
        return (level, entity, random) -> {
            Optional<Holder<Enchantment>> enchantment = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getRandomElementOf(tag, random);
            if (enchantment.isPresent()) {
                final int maxLevel = enchantment.orElseThrow().value().getMaxLevel();
                return new ToMoney(
                        EnchantmentHelper.createBook(new EnchantmentInstance(enchantment.orElseThrow(), maxLevel)),
                        cost, costAmount
                ).getOffer(level, entity, random);
            } else {
                return new ToMoney(new ItemStack(Items.BOOK), cost, 1).getOffer(level, entity, random);
            }
        };
    }

    @FunctionalInterface
    public interface ItemStackSupplier {
        ItemStack get(ServerLevel level, Entity entity, RandomSource random);
    }

    public static class ToMoney implements VillagerTrades.ItemListing {
        private final ItemCost stack;
        private final int max_uses;
        private final int experience;
        private final ItemStack price;
        private final float multiplier;

        public ToMoney(ItemCost stack, ItemStack price) {
            this.stack = stack;
            this.max_uses = 999999;
            this.experience = 20;
            this.price = price;
            this.multiplier = 0.05F;
        }

        public ToMoney(ItemLike item, ItemLike sell_price) {
            this(new ItemCost(sell_price.asItem(), 1), new ItemStack(item));
        }

        public ToMoney(ItemLike item, int amount, ItemLike sell_price) {
            this(new ItemCost(sell_price.asItem(), 1), new ItemStack(item, amount));
        }

        public ToMoney(ItemLike item, int amount, ItemLike sell_price, int amount2) {
            this(new ItemCost(sell_price.asItem(), amount2), new ItemStack(item, amount));
        }

        public ToMoney(ItemStack item, ItemLike sell_price, int amount2) {
            this(new ItemCost(sell_price.asItem(), amount2), item);
        }

        @Override
        public @Nullable MerchantOffer getOffer(ServerLevel serverLevel, Entity entity, RandomSource randomSource) {
            return new MerchantOffer(this.stack, this.price, this.max_uses, this.experience, this.multiplier);
        }
    }
    public static class FromMoney implements VillagerTrades.ItemListing {
        private final ItemCost stack;
        private final int max_uses;
        private final int experience;
        private final ItemStack price;
        private final float multiplier;

        public FromMoney(ItemCost stack, ItemStack price) {
            this.stack = stack;
            this.max_uses = 999999;
            this.experience = 20;
            this.price = price;
            this.multiplier = 0.05F;
        }

        public FromMoney(ItemLike item, ItemLike buy_price) {
            this(new ItemCost(item.asItem(), 1), new ItemStack(buy_price));
        }

        public FromMoney(ItemLike item, int amount, ItemLike buy_price) {
            this(new ItemCost(item.asItem(), amount), new ItemStack(buy_price));
        }

        public FromMoney(ItemLike item, int amount, ItemLike buy_price, int amount2) {
            this(new ItemCost(item.asItem(), amount), new ItemStack(buy_price, amount2));
        }

        @Override
        public @Nullable MerchantOffer getOffer(ServerLevel serverLevel, Entity entity, RandomSource randomSource) {
            return new MerchantOffer(this.stack, this.price, this.max_uses, this.experience, this.multiplier);
        }
    }
}
