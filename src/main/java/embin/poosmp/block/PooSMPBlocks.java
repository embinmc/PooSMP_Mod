package embin.poosmp.block;

import embin.poosmp.PooSMPMod;
import embin.poosmp.block.annoyance.Annoyance;
import embin.poosmp.block.annoyance.Annoyances;
import embin.poosmp.util.Id;
import embin.poosmp.world.PooFeatures;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public class PooSMPBlocks {
    public static final Block POOP_BLOCK = register("poop_block", Block::new, copyBlock(Blocks.MUD).mapColor(DyeColor.BROWN));
    public static final Block MISSINGNO_BLOCK = register("missingno", BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(DyeColor.MAGENTA).strength(1.5F), new Item.Properties().rarity(Rarity.EPIC));
    public static final Block POOP_BRICKS = register("poop_bricks", Block::new, copyBlock(Blocks.MUD_BRICKS).mapColor(MapColor.COLOR_ORANGE));
    public static final Block POOP_BRICK_STAIRS = register("poop_brick_stairs", stairBlock(POOP_BRICKS), copyBlock(POOP_BRICKS));
    public static final Block POOP_BRICK_SLAB = register("poop_brick_slab", SlabBlock::new, copyBlock(POOP_BRICKS));
    public static final Block POOP_BRICK_WALL = register("poop_brick_wall", wallBlock(), copyBlock(POOP_BRICKS));
    public static final Block RED_NETHER_BRICK_FENCE = register("red_nether_brick_fence", FenceBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS));
    public static final Block SUS = registerAnnoyance("im_gonna_kill_myself", Annoyances.SUS, MapColor.COLOR_RED);
    public static final Block DDEDEDODEDIAMANTE_BLOCK = register("ddededodediamante_block", ddededodediamanteBlock::new, BlockBehaviour.Properties.of().mapColor(DyeColor.MAGENTA).strength(1.0F));
    public static final Block PENIS_BLOCK = register("minecraft:penis", GrassBlock::new, copyBlock(Blocks.GRASS_BLOCK));
    public static final Block BANKERS_TABLE = register("bankers_table", copyBlock(Blocks.FLETCHING_TABLE).mapColor(DyeColor.BROWN));
    // i can't bother with this right now
    // public static final Block ITEM_SHOP = register("item_shop", new ItemShopBlock(copyBlock(Blocks.IRON_BLOCK)));
    public static final Block RED_POO_BLOCK = register("red_poo_block", BlockBehaviour.Properties.of().requiresCorrectToolForDrops().mapColor(DyeColor.RED).strength(2.5F).sound(SoundType.BONE_BLOCK), new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final Block DRAGON_ANNOYANCE = registerAnnoyance("ear_destroyer_9000", Annoyances.DRAGON, MapColor.TERRACOTTA_WHITE);
    public static final Block FAKE_DIRT = register("fake_dirt", fakeBlock(Blocks.DIRT), copyBlock(Blocks.DIRT));
    public static final Block FAKE_GRASS_BLOCK = register("fake_grass_block", fakeBlock(Blocks.GRASS_BLOCK), copyBlock(Blocks.GRASS_BLOCK));
    public static final Block FAKE_STONE = register("fake_stone", fakeBlock(Blocks.STONE), copyBlock(Blocks.STONE));
    public static final Block RIGGED_STONE = register("rigged_stone", riggedBlock(Blocks.STONE), copyBlock(Blocks.STONE));
    public static final Block DIM_MOSS_BLOCK = register(
            "dim_moss_block",
            (properties) -> new BonemealableFeaturePlacerBlock(PooFeatures.DIM_MOSS_PATCH_BONEMEAL, properties),
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(0.1F)
                    .sound(SoundType.MOSS)
                    .pushReaction(PushReaction.DESTROY)
    );
    public static final Block DIM_MOSS_CARPET = register(
            "dim_moss_carpet", CarpetBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.1F).sound(SoundType.MOSS_CARPET).pushReaction(PushReaction.DESTROY)
    );
    public static final Block DEHEED_ANNOYANCE = registerAnnoyance("glue_factory", Annoyances.DEHEED, MapColor.TERRACOTTA_WHITE, Annoyances::onSoundDeheed);


    public static Block register(Function<BlockBehaviour.Properties, Block> blockFunction, String name, Item.Properties settings, BlockBehaviour.Properties blockProperties, boolean should_register_item) {
        Identifier id = Id.of(name);
        ResourceKey<Block> resourceKey = ResourceKey.create(Registries.BLOCK, id);
        Block block = Registry.register(BuiltInRegistries.BLOCK, resourceKey, blockFunction.apply(blockProperties.setId(resourceKey)));
        if (should_register_item) {
            ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, id);
            Identifier modelId = id;
            if (block instanceof ImpersonatingBlock impersonatingBlock) {
                modelId = impersonatingBlock.getBlockIdImpersonatingAs();
            }
            Registry.register(BuiltInRegistries.ITEM, id, new BlockItem(block, settings.setId(itemKey).useBlockDescriptionPrefix().modelId(modelId)));
        }
        return block;
    }

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFunction, Item.Properties settings) {
        return register(blockFunction, name, settings, BlockBehaviour.Properties.of(), true);
    }

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFunction, BlockBehaviour.Properties settings) {
        return register(blockFunction, name, new Item.Properties(), settings, true);
    }

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFunction, boolean should_register_item) {
        return register(blockFunction, name, new Item.Properties(), BlockBehaviour.Properties.of(), should_register_item);
    }

    public static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFunction) {
        return register(blockFunction, name, new Item.Properties(), BlockBehaviour.Properties.of(), true);
    }

    public static Block register(String name, BlockBehaviour.Properties properties) {
        return register(Block::new, name, new Item.Properties(), properties, true);
    }

    public static Block register(String name, BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        return register(Block::new, name, itemProperties, properties, true);
    }

    public static BlockBehaviour.Properties copyBlock(Block block) {
        return BlockBehaviour.Properties.ofFullCopy(block);
    }

    public static Function<BlockBehaviour.Properties, Block> stairBlock(Block block) {
        return settings -> new StairBlock(block.defaultBlockState(), settings);
    }

    public static Function<BlockBehaviour.Properties, Block> wallBlock() {
        return settings -> new WallBlock(settings.forceSolidOn());
    }

    public static Function<BlockBehaviour.Properties, Block> fakeBlock(Block block) {
        return settings -> new FakeBlock(block, settings.instabreak());
    }

    public static Function<BlockBehaviour.Properties, Block> riggedBlock(Block block) {
        return settings -> new RiggedBlock(block, settings.instabreak());
    }

    public static Block registerAnnoyance(String id, Annoyance annoyance, MapColor mapColor) {
        return register(id, properties -> new AnnoyanceBlock(annoyance, properties), BlockBehaviour.Properties.of().mapColor(mapColor).strength(1.0F));
    }

    public static Block registerAnnoyance(String id, Annoyance annoyance, MapColor mapColor, AnnoyanceBlock.OnSound onSound) {
        return register(id, properties -> new AnnoyanceBlock(annoyance, properties, onSound), BlockBehaviour.Properties.of().mapColor(mapColor).strength(1.0F));
    }

    public static void init() {
        PooSMPMod.LOGGER.info("Making PooSMP blocks!");
    }
}
