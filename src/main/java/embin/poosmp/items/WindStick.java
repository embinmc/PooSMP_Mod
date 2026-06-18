package embin.poosmp.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class WindStick extends CreativeSnitchItem {
    public WindStick(Properties settings) {
        super(settings);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand interactionHand) {
        if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            Projectile.spawnProjectileFromRotation((lvl, livingEntity, itemStack) -> {
                double x = serverPlayer.position().x();
                double y = serverPlayer.getEyePosition().y();
                double z = serverPlayer.position().z();
                return new WindCharge(serverPlayer, lvl, x, y, z);
            }, serverLevel, serverPlayer.getItemInHand(interactionHand), serverPlayer, 0f, 2f, 0f);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.SUCCESS;
    }
}
