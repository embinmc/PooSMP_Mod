package embin.poosmp.items;

import embin.poosmp.economy.shop.ShopCategories;
import embin.poosmp.items.component.PooSMPItemComponents;
import embin.poosmp.PooSMPMod;
import embin.poosmp.items.component.ValueComponent;
import embin.poosmp.upgrade.PooSMPKeys;
import embin.poosmp.util.Id;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Function;

public class PooSMPItems {
    public static <T extends Item> T register(String path, Function<Item.Properties, T> item, Item.Properties properties) {
        Identifier id = Id.of(path);
        return Registry.register(BuiltInRegistries.ITEM, id, item.apply(properties.setId(ResourceKey.create(Registries.ITEM, id)).modelId(id)));
    }

    public static final Item POOP_STICK = register("poop_stick", PoopStickItem::new, new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant().stacksTo(1));
    public static final Item SERVER_SAYS_WHAT_STICK = register("server_says_what_stick", ServerSaysWhatItem::new, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant());
    public static final Item BIOME_STICK = register("biome_stick", BiomeStickItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(PooSMPItemComponents.SELECTED_BIOME, "minecraft:plains"));
    public static final Item BOOM_STICK = register("boom_stick", BoomStickItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant());
    public static final Item ZOMBIE_STICK = register("zombie_stick", properties -> new MobStickItem(
        properties, EntityType.ZOMBIE,
        MobStickItem.BuiltInNames.ZOMBIE, false
    ), new Item.Properties().stacksTo(1).rarity(Rarity.RARE).fireResistant());
    public static final Item DIAMOND_SHARD = register("diamond_shard");
    public static final Item WEDDING_RING = register("wedding_ring", WeddingRingItem::new, new Item.Properties().rarity(Rarity.RARE).stacksTo(1).fireResistant());
    public static final Item RED_NETHER_BRICK = register("red_nether_brick");
    public static final Item POOP_BRICK = register("poop_brick");
    public static final Item POOPLET = food("pooplet", PooSMPFoods.POOPLET);
    public static final Item RING = register("ring");
    public static final Item TOTEM_OF_HEALTH = totem("health", healthTotemAttributes(4, ""), false);
    public static final Item WARP_STICK = warpStick("warp_stick", PooSMPKeys.HYRULE);
    public static final Item FILL_ARMOR_TRIM_TEMPLATE = register("fill_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final Item DISC_TRIFECTA_CAP = musicDisc("trifecta_cap", PooSMPJukeboxSongs.TRIFECTA_CAP, "Embin");
    public static final Item DISC_BUTTERFLIES_AND_HURRICANES_INSTRUMENTAL = musicDisc("butterflies_and_hurricanes_instrumental", PooSMPJukeboxSongs.BUTTERFLIES_AND_HURRICANES_INSTRUMENTAL, "Embin");
    public static final Item DISC_BUDDY_HOLLY = musicDisc("buddy_holly", PooSMPJukeboxSongs.BUDDY_HOLLY, "ianyourgod");
    public static final Item DISC_STEREO_MADNESS = musicDisc("stereo_madness", PooSMPJukeboxSongs.STEREO_MADNESS, "a_pc");
    public static final Item DISC_NOT_LIKE_US = musicDisc("not_like_us", PooSMPJukeboxSongs.NOT_LIKE_US, "a_pc");
    public static final Item DISC_RESISTANCE_INSTRUMENTAL = musicDisc("resistance_instrumental", PooSMPJukeboxSongs.RESISTANCE_INSTRUMENTAL, "Embin");
    public static final Item TOTEM_OF_REACH = totem("reach", reachTotemAttributes(1.0F, ""), false);
    public static final Item BLANK_MUSIC_DISC = register("blank_music_disc", Rarity.UNCOMMON);
    public static final Item ENCHANTED_TOTEM_OF_REACH = totem("reach", reachTotemAttributes(2.0F, "_enchanted"), true);
    public static final Item ENCHANTED_TOTEM_OF_HEALTH = totem("health", healthTotemAttributes(6, "_enchanted"), true);
    public static final Item DISC_BLISS_INSTRUMENTAL = musicDisc("bliss_instrumental", PooSMPJukeboxSongs.BLISS_INSTRUMENTAL, "Embin");
    public static final Item DISC_ENDLESSLY_INSTRUMENTAL = musicDisc("endlessly_instrumental", PooSMPJukeboxSongs.ENDLESSLY_INSTRUMENTAL, "Embin");
    public static final Item DISC_ENDLESSLY = musicDisc("endlessly", PooSMPJukeboxSongs.ENDLESSLY, "Embin");
    public static final Item DISC_ENDLESSLY_STEREO = musicDisc("endlessly_stereo", PooSMPJukeboxSongs.ENDLESSLY_STEREO, "Embin");
    public static final Item ZAP_STICK = register("lightning_stick", ZapStick::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final Item VILLAGER_STICK = register("villager_stick", p -> new MobStickItem(p, EntityType.VILLAGER, MobStickItem.BuiltInNames.VILLAGER), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    public static final Item CUBE_POTTERY_SHERD = register("cube_pottery_sherd");
    public static final Item POO_POTTERY_SHERD = register("poo_pottery_sherd");
    public static final Item ONE_DOLLAR_BILL = moneyItem("one_dollar_bill", 1);
    public static final Item BACON_BUCKET = register(
        "bacon_bucket",
        properties -> new MobBucketItem(EntityType.PIG, Fluids.LAVA, SoundEvents.BUCKET_EMPTY_FISH, properties),
        new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
    );
    public static final Item TWO_DOLLAR_BILL = moneyItem("two_dollar_bill", 2);
    public static final Item FIVE_DOLLAR_BILL = moneyItem("five_dollar_bill", 5);
    public static final Item TEN_DOLLAR_BILL = moneyItem("ten_dollar_bill", 10);
    public static final Item TWENTY_FIVE_DOLLAR_BILL = moneyItem("twenty_five_dollar_bill", 25);
    public static final Item FIFTY_DOLLAR_BILL = moneyItem("fifty_dollar_bill", 50);
    public static final Item HUNDRED_DOLLAR_BILL = moneyItem("hundred_dollar_bill", 100);
    //public static final Item ONE_DOLLAR_COIN = coinItem("one_dollar_coin", 100);
    //public static final Item ONE_CENT_COIN = coinItem("one_cent_coin", 1);
    //public static final Item FIVE_CENT_COIN = coinItem("five_cent_coin", 5);
    //public static final Item TEN_CENT_COIN = coinItem("ten_cent_coin", 10);
    //public static final Item TWENTY_FIVE_CENT_COIN = coinItem("twenty_five_cent_coin", 25);
    public static final Item COW_STICK = register("cow_stick", properties -> new MobStickItem(properties, EntityType.COW, MobStickItem.BuiltInNames.COW, false));
    public static final Item DISC_STORY_OF_UNDERTALE = musicDisc("story_of_undertale", PooSMPJukeboxSongs.SOU, "Cubey");
    public static final Item RAW_RED_POO = register("raw_red_poo", Rarity.UNCOMMON);
    public static final Item RED_POO_INGOT = register("red_poo_ingot", Rarity.UNCOMMON);
    public static final Item RED_POO_UPGRADE_SMITHING_TEMPLATE = register("red_poo_upgrade_smithing_template", Rarity.RARE);
    public static final Item BANANA = food("banana", PooSMPFoods.BANANA);
    public static final Item RED_POO_SWORD = register("red_poo_sword", new Item.Properties().sword(PooSMPMaterials.RED_POO, 3, -2.4f));
    public static final Item RED_POO_SHOVEL = register("red_poo_shovel", properties -> new ShovelItem(PooSMPMaterials.RED_POO, 1.5F, -3.0F, properties));
    public static final Item RED_POO_PICKAXE = register("red_poo_pickaxe", new Item.Properties().pickaxe(PooSMPMaterials.RED_POO, 1.0F, -2.8F));
    public static final Item RED_POO_AXE = register("red_poo_axe", properties -> new AxeItem(PooSMPMaterials.RED_POO, 5.0F, -3.0F, properties));
    public static final Item RED_POO_HOE = register("red_poo_hoe", properties -> new HoeItem(PooSMPMaterials.RED_POO, -3.0F, 0.0F, properties));
    public static final Item RED_POO_HELMET = register("red_poo_helmet", new Item.Properties().humanoidArmor(PooSMPMaterials.A_RED_POO, ArmorType.HELMET));
    public static final Item RED_POO_CHESTPLATE = register("red_poo_chestplate", new Item.Properties().humanoidArmor(PooSMPMaterials.A_RED_POO, ArmorType.CHESTPLATE));
    public static final Item RED_POO_LEGGINGS = register("red_poo_leggings", new Item.Properties().humanoidArmor(PooSMPMaterials.A_RED_POO, ArmorType.LEGGINGS));
    public static final Item RED_POO_BOOTS = register("red_poo_boots", new Item.Properties().humanoidArmor(PooSMPMaterials.A_RED_POO, ArmorType.BOOTS));
    public static final Item GEAR = register("gear");
    public static final Item SCREW = register("screw");
    public static final Item GLASS_SHARD = register("glass_shard");
    public static final Item MAGIC_DEVICE = register("magic_device", MagicDeviceItem::new, new Item.Properties().durability(160).attributes(magicDeviceAttributes(17.0F)).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
    public static final Item NULL_SHARD = register("null_shard", Rarity.EPIC);
    public static final Item NULL_STICK = warpStick("null_stick", PooSMPKeys.MISSINGNO);
    public static final Item JUMPSCARE_STICK = register("jumpscare_stick", new Item.Properties().component(PooSMPItemComponents.JUMPSCARE_STICK, Unit.INSTANCE).useCooldown(1f));
    public static final Item FUN_STICK = register("fun_stick", new Item.Properties().component(PooSMPItemComponents.FUN_STICK, Unit.INSTANCE).useCooldown(1f));

    public static ItemStack getBiomeStickStack(String biome) {
        ItemStack stack = new ItemStack(BIOME_STICK);
        stack.set(PooSMPItemComponents.SELECTED_BIOME, biome);
        return stack;
    }

    private static Item moneyItem(String name, double value) {
        return register(name, MoneyItem::new, new Item.Properties().component(PooSMPItemComponents.MONEY, value).component(PooSMPItemComponents.ITEM_VALUE, new ValueComponent(value, value, ShopCategories.MISC)).stacksTo(99));
    }

    private static Item musicDisc(String name, ResourceKey<JukeboxSong> song, String requester) {
        return register("music_disc/" + name, properties -> new RequestedDiscItem(requester, properties), new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(song));
    }

    private static Item register(String name, Item.Properties properties) {
        return register(name, Item::new, properties);
    }

    private static Item register(String name, Function<Item.Properties, Item> itemFunction) {
        return register(name, itemFunction, new Item.Properties());
    }

    private static Item register(String name) {
        return register(name, new Item.Properties());
    }

    private static Item register(String name, int max_count) {
        return register(name, new Item.Properties().stacksTo(max_count));
    }

    private static Item register(String name, Rarity rarity) {
        return register(name, new Item.Properties().rarity(rarity));
    }

    private static Item food(String name, FoodProperties food) {
        return register(name, new Item.Properties().food(food, Consumables.DRIED_KELP));
    }

    private static Item totem(String name, ItemAttributeModifiers attributes, boolean enchanted) {
        if (enchanted) {
            return register("enchanted_totem_of_" + name, CreativeSnitchItem::new, new Item.Properties().attributes(attributes).rarity(Rarity.RARE).stacksTo(1).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true));
        } else {
            return register("totem_of_" + name, CreativeSnitchItem::new, new Item.Properties().attributes(attributes).rarity(Rarity.UNCOMMON).stacksTo(1));
        }
    }

    private static Item warpStick(String name, ResourceKey<Level> targetDimension) {
        return register(name, WarpStick::new, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).component(PooSMPItemComponents.WARP_DIMENSION, targetDimension));
    }

    public static ItemAttributeModifiers healthTotemAttributes(int hp, String id_suffix) {
        return ItemAttributeModifiers.builder().add(
            Attributes.MAX_HEALTH,
            new AttributeModifier(
                Id.of("poosmp:health_totem_buff" + id_suffix), hp, AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.HAND
        ).build();
    }

    public static ItemAttributeModifiers reachTotemAttributes(float amount, String id_suffix) {
        return ItemAttributeModifiers.builder().add(
            Attributes.BLOCK_INTERACTION_RANGE,
            new AttributeModifier(
                Id.of("poosmp:reach_totem_blocks" + id_suffix), amount, AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.HAND
        ).add(
            Attributes.ENTITY_INTERACTION_RANGE,
            new AttributeModifier(
                Id.of("poosmp:reach_totem_entities" + id_suffix), amount, AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.HAND
        ).build();
    }

    public static void init() {
        PooSMPMod.LOGGER.info("Making PooSMP items!!!");
    }

    public static Item.Properties copyAttributes(ItemLike item) {
        ItemAttributeModifiers attributes = item.asItem().components().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        return new Item.Properties().attributes(attributes);
    }

    public static ItemAttributeModifiers magicDeviceAttributes(float amount) {
        return ItemAttributeModifiers.builder().add(
            Attributes.ENTITY_INTERACTION_RANGE,
            new AttributeModifier(
                    Id.of("poosmp:magic_device"), amount, AttributeModifier.Operation.ADD_VALUE
            ),
            EquipmentSlotGroup.HAND
        ).build();
    }
}
