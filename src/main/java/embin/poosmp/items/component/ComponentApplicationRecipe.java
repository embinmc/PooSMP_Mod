package embin.poosmp.items.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import embin.poosmp.util.SimpleRecipeSerializer;
import embin.poosmp.util.SimpleSmithingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class ComponentApplicationRecipe extends SimpleSmithingRecipe {
    private final Ingredient target, transformer;
    private final Optional<Ingredient> ingredient;
    private final DataComponentPatch componentPatch;

    public ComponentApplicationRecipe(Ingredient target, Ingredient transformer, Optional<Ingredient> ingredient, DataComponentPatch componentPatch) {
        this.target = target;
        this.transformer = transformer;
        this.ingredient = ingredient;
        this.componentPatch = componentPatch;
    }
    public static final MapCodec<ComponentApplicationRecipe> CODEC = RecordCodecBuilder.mapCodec(c -> c.group(
            Ingredient.CODEC.fieldOf("target_item").forGetter(o -> o.target),
            Ingredient.CODEC.fieldOf("transformer").forGetter(o -> o.transformer),
            Ingredient.CODEC.optionalFieldOf("ingredient").forGetter(o -> o.ingredient),
            DataComponentPatch.CODEC.fieldOf("component_patch").forGetter(o -> o.componentPatch)
    ).apply(c, ComponentApplicationRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ComponentApplicationRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.target,
            Ingredient.CONTENTS_STREAM_CODEC,
            o -> o.transformer,
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC,
            o -> o.ingredient,
            DataComponentPatch.STREAM_CODEC,
            o -> o.componentPatch,
            ComponentApplicationRecipe::new
    );
    public static final RecipeSerializer<ComponentApplicationRecipe> SERIALIZER = new SimpleRecipeSerializer<>(CODEC, STREAM_CODEC);

    @Override
    public @NonNull ItemStack assemble(SmithingRecipeInput input, HolderLookup.@NonNull Provider provider) {
        ItemStack itemStack = new ItemStack(input.base().getItemHolder(), input.base().getCount(), input.base().getComponentsPatch());
        itemStack.applyComponents(this.componentPatch);
        return itemStack;
    }

    @Override
    public @NonNull RecipeSerializer<? extends SimpleSmithingRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.createFromOptionals(List.of(
                Optional.of(this.transformer),
                Optional.of(this.target),
                this.ingredient
        ));
    }

    @Override
    public boolean matches(@NonNull SmithingRecipeInput input, @NonNull Level level) {
        return super.matches(input, level) && !input.base().has(PooSMPItemComponents.FORCE_ALLOW_IN_BACKPACK); // hardcode this idgaf
    }

    @Override
    public @NonNull Optional<Ingredient> templateIngredient() {
        return Optional.of(this.transformer);
    }

    @Override
    public @NonNull Ingredient baseIngredient() {
        return this.target;
    }

    @Override
    public @NonNull Optional<Ingredient> additionIngredient() {
        return this.ingredient;
    }
}
