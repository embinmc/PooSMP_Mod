package embin.poosmp.util;

import embin.poosmp.world.PooSMPRegistries;
import embin.poosmp.economy.shop.ShopCategory;
import embin.poosmp.upgrade.Upgrade;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

@SuppressWarnings({"NullableProblems", "SameParameterValue"})
public class PooSMPTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, Id.of(name));
        }

        public static final TagKey<Block> INCORRECT_FOR_RED_POO_TOOLS = createTag("poosmp:incorrect_for_red_poo_tools");
    }

    public static class Items {
        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Id.of(name));
        }

        public static final TagKey<Item> TOTEMS = createTag("poosmp:totems");
        public static final TagKey<Item> POOSMP_DISCS = createTag("poosmp:poosmp_discs");
        public static final TagKey<Item> STEREO_DISCS = createTag("poosmp:stereo_discs");
        public static final TagKey<Item> RED_POO_REPAIR_ITEMS = createTag("poosmp:red_poo_repair_items");
    }

    public static class Enchantments {
        private static TagKey<Enchantment> createTag(String name) {
            return TagKey.create(Registries.ENCHANTMENT, Id.of(name));
        }

        public static final TagKey<Enchantment> BANKER_TRADEABLE_LOW = createTag("banker_tradeable/low");
        public static final TagKey<Enchantment> BANKER_TRADEABLE_MID = createTag("banker_tradeable/mid");
        public static final TagKey<Enchantment> BANKER_TRADEABLE_HIGH = createTag("banker_tradeable/high");
    }

    public static class PaintingVariants {
        private static TagKey<PaintingVariant> createTag(String name) {
            return TagKey.create(Registries.PAINTING_VARIANT, Id.of(name));
        }

        public static final TagKey<PaintingVariant> POOSMP_PAINTINGS = createTag("poosmp:poosmp_paintings");
        public static final TagKey<PaintingVariant> PLACEABLE_PAINTINGS = createTag("poosmp:placeable_poosmp_paintings");
        public static final TagKey<PaintingVariant> NON_PLACEABLE_PAINTINGS = createTag("poosmp:non_placeable_poosmp_paintings");
    }

    public static class Upgrades {
        private static TagKey<Upgrade> createTag(String name) {
            return TagKey.create(PooSMPRegistries.Keys.UPGRADE, Id.of(name));
        }

        public static final TagKey<Upgrade> LIST_ORDER = createTag("poosmp:list_order");
        public static final TagKey<Upgrade> DOUBLE_ATTRIBUTE_GIVE = createTag("poosmp:double_attribute_give");
    }

    public static class ShopCategories {
        private static TagKey<ShopCategory> createTag(String name) {
            return TagKey.create(PooSMPRegistries.Keys.SHOP_CATEGORY, Id.of(name));
        }

        public static final TagKey<ShopCategory> HIDDEN = createTag("poosmp:hidden_from_shop_command");
    }
}