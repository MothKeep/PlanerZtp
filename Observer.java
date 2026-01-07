import  java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface Observer {
    void update();
}

class CaloriesObserver implements Observer{
    private final MealComponent subject;
    private int currentCalories;

    public CaloriesObserver(MealComponent subject) {
        this.subject = subject;
    }

    @Override
    public void update() {
        currentCalories = subject.getTotalCalories();
    }

    public int getCurrentCalories()
    {
        return currentCalories;
    }
}
 
class ShoppingListObserver implements Observer{
    private final DayPlan dayPlan;
    private final Map<String, Integer> shoppingList = new HashMap<>();

    public ShoppingListObserver(DayPlan dayPlan) {
        this.dayPlan = dayPlan;
    }

    @Override
    public void update() {
        shoppingList.clear();

        for (Meal meal : dayPlan.getMeals().values()) {
            IRecipe recipe = meal.getRecipe();

            for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredients().entrySet()) {
                Ingredient currentIngridient = entry.getKey();
                int amount = entry.getValue();
                
                String name = currentIngridient.getName();

                if (shoppingList.containsKey(name)) {
                    int currentAmount = shoppingList.get(name);
                    shoppingList.put(name, currentAmount + amount);
                } 
                else {
                shoppingList.put(name, amount);
                }
        }
    }
}
    public Map<String, Integer> getShoppingList() {
        return Collections.unmodifiableMap(shoppingList);
    }

}