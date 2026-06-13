package embin.poosmp.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("NullableProblems")
public class PooUtil {
    public static final Comparator<Holder<PaintingVariant>> PAINTING_COMPARATOR = Comparator.comparing(
            Holder::value, Comparator.comparingInt(PaintingVariant::area).thenComparing(PaintingVariant::width)
    );

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException("PooSMP: Cannot round to less than 0 places");
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static double roundTwo(double value) {
        return round(value, 2);
    }

    public static void getPaintingStacks(TagKey<PaintingVariant> paintingTag, HolderLookup.Provider provider, Consumer<ItemStack> consumer) {
        HolderLookup.RegistryLookup<PaintingVariant> registryLookup = provider.lookupOrThrow(Registries.PAINTING_VARIANT);
        registryLookup.listElements().filter(painting -> painting.is(paintingTag)).sorted(PooUtil.PAINTING_COMPARATOR).forEach(painting -> {
            ItemStack itemStack = new ItemStack(Items.PAINTING);
            itemStack.set(DataComponents.PAINTING_VARIANT, painting);
            consumer.accept(itemStack);
        });
    }

    public static <T> void forEachEntry(HolderLookup.Provider provider, ResourceKey<Registry<T>> registryKey, Consumer<Holder<T>> consumer) {
        provider.lookupOrThrow(registryKey).listElements().forEach(consumer);
    }

    public static <T> void forEachEntryInTag(HolderLookup.Provider provider, ResourceKey<Registry<T>> registryKey, TagKey<T> tagKey, Consumer<Holder<T>> consumer) {
        provider.lookupOrThrow(registryKey).get(tagKey).ifPresent(holders -> holders.forEach(consumer));
    }

    public static <T> T getGameRuleValue(Level level, GameRule<T> gameRule) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(gameRule);
        } else return gameRule.defaultValue();
    }

    public static <T> List<T> listFromEnumeration(final Iterator<T> enumeration) {
        return Util.make(new ArrayList<>(), list -> {
            while (enumeration.hasNext()) {
                final T element = enumeration.next();
                list.add(element);
            }
        });
    }
}
