package embin.poosmp.items;

import embin.poosmp.items.component.PooSMPItemComponents;
import embin.poosmp.upgrade.PooSMPKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Set;

public class WarpStick extends CreativeSnitchItem {
    public static int WARP_STICK_COOLDOWN = 160;

    public WarpStick(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        double player_x = user.getX();
        double player_z = user.getZ();

        ResourceKey<Level> targetDimension = itemStack.getOrDefault(PooSMPItemComponents.WARP_DIMENSION, PooSMPKeys.HYRULE);

        if (world instanceof ServerLevel currentLevel && user instanceof ServerPlayer player) {
            ServerLevel targetLevel = currentLevel.getServer().getLevel(targetDimension);
            if (targetLevel == null) {
                player.sendSystemMessage(Component.literal("Dimension not found").withStyle(ChatFormatting.RED));
                return InteractionResult.FAIL;
            }
            ServerLevel destLevel = currentLevel.dimension().equals(targetDimension) ? currentLevel.getServer().overworld() : targetLevel;
            LevelChunk chunk = destLevel.getChunk(SectionPos.blockToSectionCoord(player_x), SectionPos.blockToSectionCoord(player_z));
            destLevel.startTickingChunk(chunk);
            int h = destLevel.getHeight(Heightmap.Types.WORLD_SURFACE, user.getBlockX(), user.getBlockZ());
            user.teleportTo(destLevel, player_x, h, player_z, Set.of(), user.getYRot(), user.getXRot(), true);
            player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 8, 5, false, true));
        }

        user.awardStat(Stats.ITEM_USED.get(this));
        user.getCooldowns().addCooldown(user.getItemInHand(hand), WARP_STICK_COOLDOWN);
        return InteractionResult.SUCCESS;
    }
}
