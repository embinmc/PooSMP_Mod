package embin.poosmp.economy;

import embin.poosmp.economy.shop.ShopCategories;
import embin.poosmp.economy.shop.ShopCategory;
import embin.poosmp.items.PooSMPItems;
import embin.poosmp.items.component.PooSMPItemComponents;
import embin.poosmp.items.component.ValueComponent;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class ItemWorth {

    public static void setPrice(DefaultItemComponentEvents.ModifyContext modifyContext, ShopCategory category, Item item, double price, double sellPrice) {
        modifyContext.modify(item, builder -> builder.set(PooSMPItemComponents.ITEM_VALUE, new ValueComponent(price, sellPrice, category)));
    }

    public static void setPrice(DefaultItemComponentEvents.ModifyContext modifyContext, ShopCategory category, Item item, double price) {
        setPrice(modifyContext, category, item, price, price / 2D);
    }

    public static void setPrices(DefaultItemComponentEvents.ModifyContext c) {
        setPrice(c, ShopCategories.MATERIALS, Items.IRON_INGOT, 4.50);
        setPrice(c, ShopCategories.MATERIALS, Items.IRON_NUGGET, 0.5, 0.2);
        setPrice(c, ShopCategories.MATERIALS, Items.GOLD_INGOT, 8.50);
        setPrice(c, ShopCategories.MATERIALS, Items.GOLD_NUGGET, 0.9, 0.4);
        setPrice(c, ShopCategories.MATERIALS, Items.DIAMOND, 18);
        setPrice(c, ShopCategories.MATERIALS, PooSMPItems.DIAMOND_SHARD, 1.5, 1);
        setPrice(c, ShopCategories.MATERIALS, Items.COPPER_INGOT, 2.25);
        setPrice(c, ShopCategories.MATERIALS, Items.COPPER_NUGGET, 0.25);
        setPrice(c, ShopCategories.NATURE, Items.GRASS_BLOCK, 0.1, 0.05);
        setPrice(c, ShopCategories.NATURE, Items.DIRT, 0.05, 0.02);
        setPrice(c, ShopCategories.NATURE, Items.STONE, 0.1, 0.03);
        setPrice(c, ShopCategories.NATURE, Items.NETHERRACK, 0.55, 0.1);
        setPrice(c, ShopCategories.NATURE, Items.END_STONE, 1.5, 1);
        setPrice(c, ShopCategories.NATURE, Items.COBBLESTONE, 0.25, 0.03);
        setPrice(c, ShopCategories.MATERIALS, Items.EMERALD, 8);
        setPrice(c, ShopCategories.MATERIALS, Items.NETHERITE_SCRAP, 400);
        setPrice(c, ShopCategories.MATERIALS, Items.NETHERITE_INGOT, 1600);
        setPrice(c, ShopCategories.FOOD, Items.APPLE, 0.8, 0.2);
        setPrice(c, ShopCategories.MATERIALS, Items.IRON_BLOCK, 40.50);
        setPrice(c, ShopCategories.MATERIALS, Items.GOLD_BLOCK, 76.50);
        setPrice(c, ShopCategories.MATERIALS, Items.DIAMOND_BLOCK, 162);
        setPrice(c, ShopCategories.MATERIALS, Items.COPPER_BLOCK, 20.25);
        setPrice(c, ShopCategories.MATERIALS, Items.WAXED_COPPER_BLOCK, 20.25);
        setPrice(c, ShopCategories.MATERIALS, Items.EMERALD_BLOCK, 72);
        setPrice(c, ShopCategories.NATURE, Items.CACTUS, 1);
        setPrice(c, ShopCategories.FOOD, Items.WHEAT, 0.8, 0.6);
        setPrice(c, ShopCategories.FOOD, Items.CARROT, 0.4);
        setPrice(c, ShopCategories.FOOD, Items.POTATO, 0.4);
        setPrice(c, ShopCategories.FOOD, Items.PUMPKIN, 1.5, 1);
        setPrice(c, ShopCategories.MATERIALS, Items.REDSTONE, 1);
        setPrice(c, ShopCategories.FOOD, Items.ROTTEN_FLESH, 0.06, 0.01);
        setPrice(c, ShopCategories.MATERIALS, Items.ECHO_SHARD, 64);
        setPrice(c, ShopCategories.NATURE, Items.SAND, 0.1);
        setPrice(c, ShopCategories.NATURE, Items.RED_SAND, 0.2);
    }
}
