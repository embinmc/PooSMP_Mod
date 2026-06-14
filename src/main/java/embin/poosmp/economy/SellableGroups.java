package embin.poosmp.economy;

import embin.poosmp.util.Id;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface SellableGroups {
    TagKey<Item> MOSS = of("moss");

    private static TagKey<Item> of(String name) {
        return TagKey.create(Registries.ITEM, Id.of("sellable_group/" + name));
    }
}
