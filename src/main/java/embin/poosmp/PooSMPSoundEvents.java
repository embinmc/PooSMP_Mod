package embin.poosmp;

import embin.poosmp.util.Id;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class PooSMPSoundEvents {
    private PooSMPSoundEvents() {}

    public static final SoundEvent MUSIC_DISC_TRIFECTA_CAP = registerSound("music_disc.trifecta_cap");
    public static final SoundEvent MUSIC_DISC_BUTTERFLIES_AND_HURRICANES_INSTRUMENTAL = registerSound("music_disc.butterflies_and_hurricanes_instrumental");
    public static final SoundEvent MUSIC_DISC_BUDDY_HOLLY = registerSound("music_disc.buddy_holly");
    public static final SoundEvent MUSIC_DISC_STEREO_MADNESS = registerSound("music_disc.stereo_madness");
    public static final SoundEvent MUSIC_DISC_NOT_LIKE_US = registerSound("music_disc.not_like_us");
    public static final SoundEvent MUSIC_DISC_RESISTANCE_INSTRUMENTAL = registerSound("music_disc.resistance_instrumental");
    public static final SoundEvent MUSIC_DISC_BLISS_INSTRUMENTAL = registerSound("music_disc.bliss_instrumental");
    public static final SoundEvent MUSIC_DISC_ENDLESSLY_INSTRUMENTAL = registerSound("music_disc.endlessly_instrumental");
    public static final SoundEvent MUSIC_DISC_ENDLESSLY = registerSound("music_disc.endlessly");
    public static final SoundEvent MUSIC_DISC_ENDLESSLY_STEREO = registerSound("music_disc.endlessly.stereo");
    public static final SoundEvent SUS = registerSound("sus");
    public static final SoundEvent MUSIC_DISC_SOU = registerSound("music_disc.story_of_undertale");
    public static final SoundEvent OOBLEP = registerSound("annoyance.ooblep");

    private static SoundEvent registerSound(String namespace) {
        Identifier id = Id.of(namespace);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void init() {
        PooSMPMod.LOGGER.info("Registering PooSMP Mod sounds! Help me.");
    }
}
