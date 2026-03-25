package embin.poosmp;

import embin.poosmp.block.PooSMPBlocks;
import embin.poosmp.items.MobStickItem;
import embin.poosmp.items.PooSMPItems;
import embin.poosmp.items.component.PooSMPItemComponents;
import embin.poosmp.util.Id;
import embin.poosmp.util.PooSMPTags;
import embin.poosmp.util.PooUtil;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class PooSMPItemGroups {
    public static void init() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Id.of("poosmp_items"), POOSMP_ITEMS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Id.of("poosmp_biome_sticks"), BIOME_STICKS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Id.of("poosmp_music_discs"), MUSIC_DISCS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Id.of("poosmp_mob_sticks"), MOB_STICKS);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Id.of("poosmp_money"), MONEY);
    }

    public static final CreativeModeTab POOSMP_ITEMS = FabricItemGroup.builder()
            .icon(() -> new ItemStack(PooSMPItems.POOPLET))
            .title(Component.literal("PooSMP"))
            .displayItems(((displayContext, entries) -> {
                HolderLookup.Provider provider = displayContext.holders();
                entries.accept(PooSMPBlocks.POOP_BLOCK);
                entries.accept(PooSMPItems.POOP_STICK);
                entries.accept(PooSMPItems.SERVER_SAYS_WHAT_STICK);
                entries.accept(PooSMPItems.BIOME_STICK);
                entries.accept(PooSMPItems.BOOM_STICK);
                entries.accept(PooSMPItems.ZOMBIE_STICK);
                entries.accept(PooSMPItems.DIAMOND_SHARD);
                entries.accept(PooSMPBlocks.MISSINGNO_BLOCK);
                entries.accept(PooSMPItems.WEDDING_RING);
                entries.accept(PooSMPItems.RED_NETHER_BRICK);
                entries.accept(PooSMPItems.POOP_BRICK);
                entries.accept(PooSMPBlocks.POOP_BRICKS);
                entries.accept(PooSMPItems.POOPLET);
                entries.accept(PooSMPBlocks.POOP_BRICK_STAIRS);
                entries.accept(PooSMPBlocks.POOP_BRICK_SLAB);
                entries.accept(PooSMPBlocks.POOP_BRICK_WALL);
                entries.accept(PooSMPItems.RING);
                entries.accept(PooSMPBlocks.RED_NETHER_BRICK_FENCE);
                entries.accept(PooSMPItems.TOTEM_OF_HEALTH);
                entries.accept(PooSMPItems.WARP_STICK);
                entries.accept(PooSMPItems.FILL_ARMOR_TRIM_TEMPLATE);
                entries.accept(PooSMPItems.DISC_TRIFECTA_CAP);
                entries.accept(PooSMPItems.DISC_BUTTERFLIES_AND_HURRICANES_INSTRUMENTAL);
                entries.accept(PooSMPItems.DISC_BUDDY_HOLLY);
                entries.accept(PooSMPItems.DISC_STEREO_MADNESS);
                entries.accept(PooSMPItems.DISC_NOT_LIKE_US);
                entries.accept(PooSMPItems.DISC_RESISTANCE_INSTRUMENTAL);
                entries.accept(PooSMPItems.TOTEM_OF_REACH);
                entries.accept(PooSMPItems.BLANK_MUSIC_DISC);
                entries.accept(PooSMPItems.ENCHANTED_TOTEM_OF_HEALTH);
                entries.accept(PooSMPItems.ENCHANTED_TOTEM_OF_REACH);
                entries.accept(PooSMPItems.DISC_BLISS_INSTRUMENTAL);
                entries.accept(PooSMPItems.DISC_ENDLESSLY_INSTRUMENTAL);
                entries.accept(PooSMPItems.DISC_ENDLESSLY);
                entries.accept(PooSMPItems.DISC_ENDLESSLY_STEREO);
                entries.accept(PooSMPItems.ZAP_STICK);
                entries.accept(PooSMPItems.VILLAGER_STICK);
                entries.accept(PooSMPBlocks.SUS);
                entries.accept(PooSMPBlocks.DDEDEDODEDIAMANTE_BLOCK);
                entries.accept(PooSMPItems.BACON_BUCKET);
                entries.accept(PooSMPItems.COW_STICK);
                entries.accept(PooSMPBlocks.BANKERS_TABLE);
                PooUtil.getPaintingStacks(PooSMPTags.PaintingVariants.PLACEABLE_PAINTINGS, provider, entries::accept);
                entries.accept(PooSMPItems.DISC_STORY_OF_UNDERTALE);
                entries.accept(PooSMPItems.RAW_RED_POO);
                entries.accept(PooSMPItems.RED_POO_INGOT);
                entries.accept(PooSMPBlocks.RED_POO_BLOCK);
                entries.accept(PooSMPItems.RED_POO_UPGRADE_SMITHING_TEMPLATE);
                entries.accept(PooSMPItems.BANANA);
                entries.accept(PooSMPItems.RED_POO_SWORD);
                entries.accept(PooSMPItems.RED_POO_PICKAXE);
                entries.accept(PooSMPItems.RED_POO_AXE);
                entries.accept(PooSMPItems.RED_POO_SHOVEL);
                entries.accept(PooSMPItems.RED_POO_HOE);
                entries.accept(PooSMPItems.RED_POO_HELMET);
                entries.accept(PooSMPItems.RED_POO_CHESTPLATE);
                entries.accept(PooSMPItems.RED_POO_LEGGINGS);
                entries.accept(PooSMPItems.RED_POO_BOOTS);
                entries.accept(PooSMPItems.GEAR);
                entries.accept(PooSMPItems.SCREW);
                entries.accept(PooSMPItems.GLASS_SHARD);
                entries.accept(PooSMPItems.MAGIC_DEVICE);
                entries.accept(PooSMPBlocks.DRAGON_ANNOYANCE);
                PooUtil.getPaintingStacks(PooSMPTags.PaintingVariants.NON_PLACEABLE_PAINTINGS, provider, entries::accept);
                entries.accept(PooSMPItems.NULL_SHARD);
                entries.accept(PooSMPBlocks.FAKE_DIRT);
                entries.accept(PooSMPBlocks.FAKE_GRASS_BLOCK);
                entries.accept(PooSMPBlocks.FAKE_STONE);
                entries.accept(PooSMPBlocks.RIGGED_STONE);
                entries.accept(PooSMPItems.NULL_STICK);
                entries.accept(PooSMPItems.JUMPSCARE_STICK);
                entries.accept(PooSMPItems.FUN_STICK);
            })).build();

    public static final CreativeModeTab BIOME_STICKS = FabricItemGroup.builder()
            .icon(() -> new ItemStack(PooSMPItems.BIOME_STICK))
            .title(Component.literal("PooSMP: Biome Sticks"))
            .displayItems((displayContext, entries) -> {
                HolderLookup.Provider provider = displayContext.holders();
                PooUtil.forEachEntry(provider, Registries.BIOME, biomeHolder -> {
                    ItemStack itemStack = new ItemStack(PooSMPItems.BIOME_STICK);
                    itemStack.set(PooSMPItemComponents.SELECTED_BIOME, biomeHolder.getRegisteredName());
                    entries.accept(itemStack);
                });
            }).build();

    public static final CreativeModeTab MUSIC_DISCS = FabricItemGroup.builder()
            .icon(() -> new ItemStack(PooSMPItems.DISC_TRIFECTA_CAP))
            .title(Component.literal("PooSMP: Music Discs"))
            .displayItems((displayContext, entries) -> {
                HolderLookup.Provider provider = displayContext.holders();
                PooUtil.forEachEntryInTag(provider, Registries.ITEM, PooSMPTags.Items.POOSMP_DISCS, ItemStack::new);
            }).build();

    public static final CreativeModeTab MOB_STICKS = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.ZOMBIE_HEAD))
            .title(Component.literal("PooSMP: Mob Sticks"))
            .displayItems((displayContext, entries) -> {
                HolderLookup.Provider provider = displayContext.holders();
                MobStickItem.Stack.getStacks(provider, entries::accept);
            }).build();

    public static final CreativeModeTab MONEY = FabricItemGroup.builder()
            .icon(() -> new ItemStack(PooSMPItems.ONE_DOLLAR_BILL))
            .title(Component.literal("PooSMP: Money"))
            .displayItems((displayContext, entries) -> {
                entries.accept(PooSMPBlocks.BANKERS_TABLE);
                entries.accept(PooSMPItems.ONE_DOLLAR_BILL);
                entries.accept(PooSMPItems.TWO_DOLLAR_BILL);
                entries.accept(PooSMPItems.FIVE_DOLLAR_BILL);
                entries.accept(PooSMPItems.TEN_DOLLAR_BILL);
                entries.accept(PooSMPItems.TWENTY_FIVE_DOLLAR_BILL);
                entries.accept(PooSMPItems.FIFTY_DOLLAR_BILL);
                entries.accept(PooSMPItems.HUNDRED_DOLLAR_BILL);
            }).build();
}
