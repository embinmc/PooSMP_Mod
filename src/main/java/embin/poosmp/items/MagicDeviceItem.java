package embin.poosmp.items;

import embin.poosmp.util.PooUtil;
import embin.poosmp.world.PooSMPGameRules;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MagicDeviceItem extends Item {
    public static final float EXPLOSION_POWER = 3.25f;

    public MagicDeviceItem(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        user.awardStat(Stats.ITEM_USED.get(this));
        if (world instanceof ServerLevel serverLevel) {
            if (serverLevel.getGameRules().get(PooSMPGameRules.OUTLAW_EXPLOSIVE_GADGETS)) {
                user.displayClientMessage(PooUtil.OUTLAWED_EXPLOSIVE_TEXT, true);
                return InteractionResult.FAIL;
            }
            HitResult hitResult = user.pick(20.0D, 0.0F, false);
            DamageSources damageSources = new DamageSources(world.registryAccess());
            ExplosionDamageCalculator eb = new ExplosionDamageCalculator();
            Vec3 size = new Vec3(1, 1, 1);
            Vec3 size2 = new Vec3(20, 20, 20);
            EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(user, size, size2, user.getBoundingBox(), e -> !e.isSpectator() && e.isPickable(), 20);
            if (itemStack.has(DataComponents.CUSTOM_NAME)) {
                String victim_name = itemStack.get(DataComponents.CUSTOM_NAME).getString();
                Player victim = getPlayerByName(victim_name, world);
                if (victim != null) {
                    world.explode(null, damageSources.explosion(null, null), eb, victim.getEyePosition(), EXPLOSION_POWER, false, Level.ExplosionInteraction.TRIGGER);
                } else {
                    user.displayClientMessage(Component.literal("No player with such name exists!").withStyle(ChatFormatting.YELLOW), true);
                }
            } else if (entityHitResult != null && entityHitResult.getEntity() != null) {
                Vec3 pos = entityHitResult.getEntity().getEyePosition();
                world.explode(null, damageSources.explosion(null, null), eb, pos, EXPLOSION_POWER, false, Level.ExplosionInteraction.TRIGGER);
            } else if (hitResult.getType() == HitResult.Type.BLOCK) {
                explodeBlock(hitResult, world);
            } else {
                Vec3 pos = hitResult.getLocation();
                world.explode(null, damageSources.explosion(null, null), eb, pos, EXPLOSION_POWER, false, Level.ExplosionInteraction.TRIGGER);
            }
            itemStack.hurtAndBreak(1, user, hand);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        Level world = user.level();
        if (!world.isClientSide()) {
            Vec3 pos = entity.getEyePosition();
            DamageSources damageSources = new DamageSources(world.registryAccess());
            ExplosionDamageCalculator eb = new ExplosionDamageCalculator();
            world.explode(null, damageSources.explosion(null, null), eb, pos, EXPLOSION_POWER, false, Level.ExplosionInteraction.TRIGGER);
            stack.hurtAndBreak(1, user, hand);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    public static Player getPlayerByName(String name, Level world) {
        for (Player player : world.players()) {
            if (Objects.equals(name, player.getScoreboardName())) {
                return player;
            }
        }
        return null;
    }

    private static void explodeBlock(HitResult hitResult, Level world) {
        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
        double x = blockPos.getX();
        double y = blockPos.getY() + 1.0D;
        double z = blockPos.getZ();
        world.explode(null, x, y, z, EXPLOSION_POWER, Level.ExplosionInteraction.TRIGGER);
    }
}
