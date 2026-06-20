package embin.poosmp.client;

import com.mojang.blaze3d.platform.InputConstants;
import embin.poosmp.block.PooSMPBlocks;
import embin.poosmp.client.screen.upgrade.UpgradesScreen;
import embin.poosmp.networking.PooSMPMessages;
import embin.poosmp.util.Id;
import embin.poosmp.world.PooSMPSavedData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.Locale;

public class PooSMPModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("poosmp/client");
    public static final KeyMapping.Category POOSMP_KEYS = KeyMapping.Category.register(Id.of("poosmp_keys"));
    public static final boolean SYNC_DATA = true;
    public static final boolean ALWAYS_SHOW_BALANCE = true;

	public static KeyMapping openUpgradesScreen = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.poosmp.open_upgrades_screen",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_0,
			POOSMP_KEYS
	));
	public static KeyMapping openDeathScreen = KeyBindingHelper.registerKeyBinding(new KeyMapping(
			"key.poosmp.open_death_screen",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_KP_DIVIDE,
            POOSMP_KEYS
	));

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(Id.of("open_upgrades_screen"), client -> {
			while (openUpgradesScreen.consumeClick()) {
				client.setScreen(new UpgradesScreen(client.screen));
			}
			while (openDeathScreen.consumeClick()) {
                if (client.player != null) {
                    client.setScreen(new DeathScreen(Component.literal("nah jk (click title screen to respawn)"), false, client.player));
                    for (AttributeInstance attribute : client.player.getAttributes().getSyncableAttributes()) {
                        for (AttributeModifier modifier : attribute.getModifiers()) {
                            LOGGER.info("{} / {}", attribute.getAttribute().getRegisteredName(), modifier.id().toString());
                        }
                    }
                }
			}
		});

		//ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
		//	dispatcher.register(
		//			ClientCommandManager.literal("shop").executes(context -> {
		//				FabricClientCommandSource source = context.getSource();
		//				PlayerInventory inv = source.getPlayer().getInventory();
		//				source.getClient().setScreen(new ShopScreenOld(new ShopScreenHandler(0, inv), inv));
		//				return 1;
		//			})
		//	);
		//});


		BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT,
			Blocks.MOSS_CARPET,
			Blocks.RED_CARPET,
			Blocks.YELLOW_CARPET,
			Blocks.ORANGE_CARPET,
			Blocks.BLACK_CARPET,
			Blocks.BLUE_CARPET,
			Blocks.CYAN_CARPET,
			Blocks.LIGHT_BLUE_CARPET,
			Blocks.LIME_CARPET,
			Blocks.LIGHT_GRAY_CARPET,
			Blocks.GRAY_CARPET,
			Blocks.BROWN_CARPET,
			Blocks.GREEN_CARPET,
			Blocks.MAGENTA_CARPET,
			Blocks.PINK_CARPET,
			Blocks.MAGENTA_CARPET,
			Blocks.WHITE_CARPET,
            PooSMPBlocks.FAKE_GRASS_BLOCK
		);

		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getAverageGrassColor(world, pos) : GrassColor.getDefaultColor(),
			PooSMPBlocks.FAKE_GRASS_BLOCK
		);

        HudElementRegistry.addLast(Id.of("balance"), (guiGraphics, deltaTracker) -> {
            Minecraft minecraft = Minecraft.getInstance();
            PooSMPSavedData savedData = PooSMPSavedData.Client.INSTANCE;
            if (minecraft.player != null) {
                if (savedData.getBalance(minecraft.player) > 0 || PooSMPModClient.ALWAYS_SHOW_BALANCE) {
                    String balance = NumberFormat.getCurrencyInstance(Locale.US).format(savedData.getBalance(minecraft.player));
                    guiGraphics.drawString(minecraft.font, balance, 4, guiGraphics.guiHeight() - 10, CommonColors.WHITE);
                }
            }
        });

		// ColorProviderRegistry.ITEM.register(
		// 	(stack, tintIndex) -> GrassColors.getColor(0.5, 1.0),
		// 	PooSMPBlocks.FAKE_GRASS_BLOCK
		// );

		PooSMPMessages.registerS2CPackets();
	}
}