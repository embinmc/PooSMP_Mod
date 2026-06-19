package embin.poosmp.block;

import embin.poosmp.block.annoyance.Annoyance;
import embin.poosmp.world.PooSMPGameRules;
import embin.poosmp.world.PooSMPRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Objects;
import java.util.function.Consumer;

public class AnnoyanceBlock extends Block implements BlockWithTooltip {
    protected final Annoyance annoyance;
    protected final OnSound onSound;

    public AnnoyanceBlock(Annoyance annoyance, BlockBehaviour.Properties settings, OnSound onSound) {
        super(settings);
        this.annoyance = annoyance;
        this.onSound = onSound;
    }

    public AnnoyanceBlock(Annoyance annoyance, BlockBehaviour.Properties settings) {
        this(annoyance, settings, OnSound.DO_NOTHING);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!world.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) world;
            if (serverLevel.getGameRules().get(PooSMPGameRules.ANNOYANCES_MAKE_SOUND)) {
                BlockPos blockPos = hit.getBlockPos();
                float v = this.annoyance.getVolume() * 1.5F;
                float p = this.annoyance.getPitch() * 1.5F;
                world.playSound(null, blockPos, this.annoyance.getSound(), SoundSource.BLOCKS, v, p);
                this.onSound.onSound(serverLevel, state, blockPos);
            }
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (serverLevel.getGameRules().get(PooSMPGameRules.ANNOYANCES_MAKE_SOUND)) {
            if (random.nextIntBetweenInclusive(1, 100) <= this.annoyance.getChance()) {
                serverLevel.playSound(null, pos, this.annoyance.getSound(), SoundSource.BLOCKS, this.annoyance.getVolume(), this.annoyance.getPitch());
                this.onSound.onSound(serverLevel, state, pos);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag type) {
        if (type.isAdvanced()) {
            Identifier annoyanceId = Objects.requireNonNull(PooSMPRegistries.ANNOYANCE.getKey(this.annoyance));
            consumer.accept(Component.literal("Annoyance: " + annoyanceId).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @FunctionalInterface
    public interface OnSound {
        void onSound(final ServerLevel level, BlockState state, BlockPos pos);

        OnSound DO_NOTHING = (level, state, pos) -> {};
    }
}
