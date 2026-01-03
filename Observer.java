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
/* nie działa jeszcze 
class ShoppingListObserver implements Observer{
    private final DayPlan dayPlan;
    private final Set<String> shoppingList = new HashSet<>();

    public ShoppingListObserver(DayPlan dayPlan) {
        this.dayPlan = dayPlan;
    }

    @Override
    public void update() {
        shoppingList.clear();

        for(Meal meal: dayPlan.getMeals().values()) {
            //shoppingList.addAll(meal.getRecipe().c
        }
    }

    public Set<String> getShoppingList() {
        return Collections.unmodifiableSet(shoppingList);
    }
}*/