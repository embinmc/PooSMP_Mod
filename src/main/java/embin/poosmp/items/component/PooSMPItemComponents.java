package embin.poosmp.items.component;

import com.mojang.serialization.Codec;
import embin.poosmp.PooSMPMod;
import embin.poosmp.util.Id;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class PooSMPItemComponents {
    public static void init() {
        PooSMPMod.LOGGER.info("Making PooSMP item components!!!");
    }

    public static final DataComponentType<String> SELECTED_BIOME = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("selected_biome"),
        DataComponentType.<String>builder().persistent(Codec.STRING).build()
    );

    public static final DataComponentType<Boolean> MARRIED = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("married"),
        DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );

    public static final DataComponentType<Boolean> FROM_CREATIVE = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("from_creative"),
        DataComponentType.<Boolean>builder().persistent(Codec.BOOL).build()
    );

    public static final DataComponentType<Unit> FORCE_MARRIED = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("force_married"),
        DataComponentType.<Unit>builder().persistent(Unit.CODEC).build()
    );

    public static final DataComponentType<EntityType<?>> MOB_OVERRIDE = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("mob_override"),
        DataComponentType.<EntityType<?>>builder().persistent(BuiltInRegistries.ENTITY_TYPE.byNameCodec()).build()
    );

    public static final DataComponentType<List<String>> MOB_NAMES = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("mob_stick_names_override"),
        DataComponentType.<List<String>>builder().persistent(Codec.STRING.listOf()).build()
    );

    public static final DataComponentType<ItemStack> MOB_OFFHAND = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("mob_offhand_item_override"),
        DataComponentType.<ItemStack>builder().persistent(ItemStack.OPTIONAL_CODEC).build()
    );

    public static final DataComponentType<Double> MONEY = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("money"),
        DataComponentType.<Double>builder().persistent(Codec.DOUBLE).build()
    );

    public static final DataComponentType<Integer> STICK_COOLDOWN_OVERRIDE = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("stick_cooldown_override"),
        DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );

    public static final DataComponentType<Integer> BIOME_STICK_RADIUS_OVERRIDE = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Id.of("biome_stick_radius_override"),
        DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );

    public static final DataComponentType<Identifier> DISPLAYED_ID = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Id.of("displayed_id"),
            DataComponentType.<Identifier>builder().persistent(Identifier.CODEC).build()
    );

    public static final DataComponentType<ValueComponent> ITEM_VALUE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Id.of("item_value"),
            DataComponentType.<ValueComponent>builder().persistent(ValueComponent.CODEC).build()
    );

    public static final DataComponentType<ResourceKey<Level>> WARP_DIMENSION = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Id.of("warp_dimension"),
            DataComponentType.<ResourceKey<Level>>builder().persistent(Level.RESOURCE_KEY_CODEC).build()
    );

    public static final DataComponentType<Unit> JUMPSCARE_STICK = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Id.of("jumpscare_stick"),
            DataComponentType.<Unit>builder().persistent(Unit.CODEC).build()
    );

    public static final DataComponentType<Unit> FUN_STICK = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Id.of("fun_stick"),
            DataComponentType.<Unit>builder().persistent(Unit.CODEC).build()
    );

    public static final DataComponentType<Unit> FORCE_ALLOW_IN_BACKPACK = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Id.of("force_allow_in_backpack"),
            DataComponentType.<Unit>builder().persistent(Unit.CODEC).build()
    );
}
