package embin.poosmp.items;

import embin.poosmp.util.PooUtil;
import embin.poosmp.world.PooSMPGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class BoomStickItem extends Item {
    public static int BOOM_STICK_COOLDOWN = 20;
    public BoomStickItem(Properties settings) {
        super(settings);
    }

    @Override
    public @NonNull InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        user.awardStat(Stats.ITEM_USED.get(this));
        user.getCooldowns().addCooldown(itemStack, BOOM_STICK_COOLDOWN);
        if (world instanceof ServerLevel serverLevel) {
            if (serverLevel.getGameRules().get(PooSMPGameRules.OUTLAW_EXPLOSIVE_GADGETS)) {
                user.displayClientMessage(PooUtil.OUTLAWED_EXPLOSIVE_TEXT, true);
                return InteractionResult.FAIL;
            }
            double player_x = user.getX();
            double player_z = user.getZ();
            double player_y;
            BlockPos pos = BlockPos.containing(player_x, user.getY() - 1, player_z);
            if (world.getBlockState(pos).isRedstoneConductor(world, pos)) {
                player_y = user.getY();
            } else {
                player_y = user.getY() - 1;
            }
            user.setInvulnerable(true);
            world.explode(null, player_x, player_y, player_z, 3.5F, Level.ExplosionInteraction.NONE);
            user.setInvulnerable(false);
        }
        return InteractionResult.SUCCESS;
    }
}
