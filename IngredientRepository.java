import java.util.HashMap;
import java.util.Map;

public class IngredientRepository 
{
    private static final Map<String, Ingredient> ingredients = new HashMap<>();

    public static void register(Ingredient ingredient)
    {
        ingredients.put(ingredient.getName().toLowerCase(), ingredient);
    }

    public static Ingredient get(String name)
    {
        return ingredients.get(name.toLowerCase());
    }

    public static void clear()
    {
        ingredients.clear();
    }
}
