package embin.poosmp.block.annoyance;

import embin.poosmp.world.PooSMPRegistries;
import embin.poosmp.PooSMPSoundEvents;
import embin.poosmp.util.Id;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

public class Annoyances {
    public static final Annoyance SUS = register("sus", PooSMPSoundEvents.SUS, 1f, 1f, 80);
    public static final Annoyance DRAGON = register("dragon", SoundEvents.ENDER_DRAGON_DEATH, 1.25f, 1f, 35);
    public static final Annoyance DEHEED = register("deheed", SoundEvents.HORSE_DEATH, 1.2f, 1);
    public static final Annoyance OBLONG = register("oblong", SoundEvents.CAMEL_HUSK_DEATH, 1.2f, 0.9f, 80);
    public static final Annoyance OOBLEP = register("ooblep", PooSMPSoundEvents.OOBLEP, 1f, 1f, 90);

    public static Annoyance register(Identifier id, SoundEvent soundEvent, float volume, float pitch, int chance) {
        return Registry.register(PooSMPRegistries.ANNOYANCE, id, new Annoyance(soundEvent, volume, pitch, chance));
    }

    private static Annoyance register(String id, SoundEvent soundEvent, float volume, float pitch, int chance) {
        return register(Id.of(id), soundEvent, volume, pitch, chance);
    }

    private static Annoyance register(String id, SoundEvent soundEvent, float volume, float pitch) {
        return register(id, soundEvent, volume, pitch, 100);
    }

    private static Annoyance register(String id, SoundEvent soundEvent) {
        return register(id, soundEvent, 1f, 1f);
    }

    public static void onSoundDeheed(ServerLevel level, BlockState state, BlockPos pos) {
        ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.LEATHER));
        level.addFreshEntity(item);
    }
}
