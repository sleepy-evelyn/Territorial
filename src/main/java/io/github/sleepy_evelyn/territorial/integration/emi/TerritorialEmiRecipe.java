package io.github.sleepy_evelyn.territorial.integration.emi;


import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class TerritorialEmiRecipe implements EmiRecipe {
    private final EmiRecipeCategory category;
    private final Identifier id;
    protected List<EmiIngredient> input = List.of();
    protected List<EmiIngredient> catalysts = List.of();
    protected List<EmiStack> output = List.of();
    private final String group;

    public TerritorialEmiRecipe(EmiRecipeCategory category, Identifier id, String group) {
        this.category = category;
        this.id = id;
        this.group = group;
    }

    public TerritorialEmiRecipe(EmiRecipeCategory category, Recipe<?> recipe) {
        this(category, recipe.getId(), recipe.getGroup());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    public String getGroup() {
        return group;
    }
}
