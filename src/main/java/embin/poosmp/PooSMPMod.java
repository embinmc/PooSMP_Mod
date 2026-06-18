package embin.poosmp;

import embin.poosmp.block.PooSMPBlocks;
import embin.poosmp.economy.ItemWorth;
import embin.poosmp.economy.shop.ShopCategories;
import embin.poosmp.items.ItemUses;
import embin.poosmp.items.PooSMPItems;
import embin.poosmp.items.component.PooSMPItemComponents;
import embin.poosmp.networking.PooSMPMessages;
import embin.poosmp.upgrade.Upgrade;
import embin.poosmp.util.*;
import embin.poosmp.villager.PooSMPPoi;
import embin.poosmp.villager.PooSMPVillagers;
import embin.poosmp.villager.TradeConstructors;
import embin.poosmp.world.PooSMPGameRules;
import embin.poosmp.world.PooSMPRegistries;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.impl.item.ComponentTooltipAppenderRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PooSMPMod implements ModInitializer {
	public static final String MOD_ID = "poosmp";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final boolean componentless_installed = FabricLoader.getInstance().isModLoaded("componentless");
	public static final boolean SHOP_ENABLED = true; // not ready yet

	public static final ResourceKey<DamageType> SUICIDE = ResourceKey.create(Registries.DAMAGE_TYPE, Id.of("suicide"));

	@Override
	public void onInitialize() {
		PooSMPRegistries.acknowledge();
		PooSMPSoundEvents.init(); // so that music discs actually work
		//Upgrades.init();
        PooSMPItems.init();
		PooSMPBlocks.init();
		PooSMPItemComponents.init();
		PooSMPItemGroups.init();
		ShopCategories.registerCategories();
        PooSMPGameRules.acknowledge();
        ItemUses.register();

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
			entries.addAfter(Items.RED_NETHER_BRICK_WALL, PooSMPBlocks.RED_NETHER_BRICK_FENCE.asItem());
		});

		PooSMPPoi.init();
		PooSMPVillagers.init();
		TradeConstructors.register_villager_trades();

		PooSMPMessages.register();
		PooSMPMessages.registerC2SPackets();

		DynamicRegistries.registerSynced(PooSMPRegistries.Keys.UPGRADE, Upgrade.CODEC);

        ComponentTooltipAppenderRegistryImpl.addBefore(DataComponents.ENCHANTMENTS, PooSMPItemComponents.ITEM_VALUE);

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> PooSMPCommands.register(dispatcher, registryAccess));

		DefaultItemComponentEvents.MODIFY.register(Id.of("poosmp:displayed_id"), modifyContext -> {
			modifyContext.modify(item -> true, (builder, item) -> {
				Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
				builder.set(PooSMPItemComponents.DISPLAYED_ID, itemId);
				//if (itemId.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
				//	builder.add(PooSMPItemComponents.DISPLAYED_ID, Identifier.of("mojang", itemId.getPath()));
				//} else {
				//	builder.add(PooSMPItemComponents.DISPLAYED_ID, itemId);
				//}
			});
		});

        ServerTickEvents.START_WORLD_TICK.register(Id.of("on_tick"), level -> {
            if (level.getRandom().nextIntBetweenInclusive(0, 6000) == 2) {
				ServerPlayer victim = level.getRandomPlayer();
				if (victim == null)
					return;
                double x = victim.getX() + 8;
                double y = victim.getY() + 3;
                double z = victim.getZ() + 4;
                SoundEvent soundEvent = switch (level.getRandom().nextIntBetweenInclusive(0, 5)) {
                    case 1 -> SoundEvents.GRAVEL_BREAK;
                    case 2 -> SoundEvents.SAND_BREAK;
                    case 3 -> SoundEvents.WOOD_BREAK;
                    case 4 -> SoundEvents.COPPER_BREAK;
                    case 5 -> SoundEvents.NETHERRACK_BREAK;
                    default -> SoundEvents.STONE_BREAK;
                };
                level.playSound(null, x, y, z, soundEvent, SoundSource.BLOCKS, 0.3f, 1f);
                int bat = level.getRandom().nextIntBetweenInclusive(0, 80);
                switch (bat) {
					case 3 -> {
						PlayerList playerList = level.getServer().getPlayerList();
						playerList.broadcastSystemMessage(Component.literal("SUPER POOP EVENT: STARTING IN 84 YEARS!!!!!"), false);
						level.getPlayers(LivingEntity::isAlive).forEach(player -> {
							player.giveExperiencePoints(4000);
							level.playSound(null, player.blockPosition(), SoundEvents.COW_DEATH, SoundSource.HOSTILE);
							level.playSound(null, player.blockPosition(), SoundEvents.HORSE_DEATH, SoundSource.HOSTILE);
							level.playSound(null, player.blockPosition(), SoundEvents.SHEEP_DEATH, SoundSource.HOSTILE);
							level.playSound(null, player.blockPosition(), SoundEvents.VILLAGER_DEATH, SoundSource.HOSTILE);
						});
					}
                    //case 4 -> level.sendSystemMessage(Component.literal("VpmNu06pT_o").withStyle(ChatFormatting.DARK_GRAY));
                    case 7, 8, 9, 44 -> {
                        Component message = Component.translatable("poosmp.shit_self", victim.getDisplayName());
                        PlayerList playerList = level.getServer().getPlayerList();
                        playerList.broadcastSystemMessage(message, false);
                    }
                    default -> {}
                }
            }
        });

		if (PooSMPMod.SHOP_ENABLED) {
			DefaultItemComponentEvents.MODIFY.register(Id.of("set_item_prices"), ItemWorth::setPrices);
		}

		LootTableEvents.MODIFY_DROPS.register(Id.of("dim"), (entry, context, drops) -> {
			if (entry.is(BuiltInLootTables.SIMPLE_DUNGEON) || entry.is(BuiltInLootTables.ANCIENT_CITY_ICE_BOX)) {
				ItemStack itemStack = new ItemStack(PooSMPItems.DIMWORLD_STICK);
				itemStack.remove(PooSMPItemComponents.FROM_CREATIVE);
				drops.add(itemStack);
			}
		});

		LOGGER.info("im all pooped up");
	}
}