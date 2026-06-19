package embin.poosmp.world;

import com.mojang.serialization.Codec;
import embin.poosmp.util.Id;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.level.gamerules.GameRule;

@SuppressWarnings({"NullableProblems", "SameParameterValue"})
public final class PooSMPGameRules {
    private PooSMPGameRules() {}

    public static final GameRule<Double> STARTING_BALANCE = ofDouble("starting_balance", 0D);
    public static final GameRule<Boolean> ANNOYANCES_MAKE_SOUND = ofBool("annoyances_make_sound", true);
    public static final GameRule<Boolean> OUTLAW_EXPLOSIVE_GADGETS = ofBool("outlaw_explosive_gadgets", false);

    private static GameRule<Boolean> ofBool(String id, boolean defaultValue) {
        return GameRuleBuilder.forBoolean(defaultValue).buildAndRegister(Id.of(id));
    }

    private static GameRule<Double> ofDouble(String id, double defaultValue) {
        return GameRuleBuilder.forDouble(defaultValue).codec(Codec.doubleRange(0, 1_000_000)).buildAndRegister(Id.of(id));
    }

    public static void acknowledge() {}
}
