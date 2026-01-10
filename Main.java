//import
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


/*
   Składniki
   Przepisy 
   Plan posiłków 

   Lista zakupów 
*/
class AppState{
  private final MealPlan mealPlan;
  private final List<Ingredient> ingredients;
    
  private final RecipeManager recipeManager;
  private final CommandController commandController;
  
  public AppState() {
    this.recipeManager = new RecipeManager();
    this.mealPlan = new MealPlan();
    this.ingredients = new ArrayList<>();
    this.commandController = new CommandController();
  }

  public RecipeManager getRecipeManager() {
    return recipeManager;
  }

  public MealPlan getMealPlan() {
    return mealPlan;
  }

  public List<Ingredient> getIngredients() {
    return Collections.unmodifiableList(ingredients);
  }

  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
  }

  public void removeIngredient(Ingredient ingredient) {
    ingredients.remove(ingredient);
  }

  public Ingredient findIngredient(String name) {
    for (Ingredient i : ingredients) {
      if (i.getName().equalsIgnoreCase(name)) {
        return i;
      }
    }
      return null;
  }

}

public class Main{
  private final AppState appState = new AppState();
  
  public static void main(String[] args){
    Scanner scanner = new Scanner(System.in);
    boolean running = true;
  
    while(running){
      System.out.println("""
        1. Edycja składników
        2. Edycja przepisów
        3. Zaplanuj konkretny dzień
        4. Skopiuj plan posiłku na inne dni
        5. Wyświetl plan
        6. Wyświetl listę zakupów
        7. Eksportuj dane
        8. Importuj dane
        0. Wyjście
      """);

      int choice = scanner.nextInt();
      switch (choice) {
                case 1 -> {
                    boolean back1 = false;
                    while (!back1) {
                        System.out.println("""
                                1. Dodaj nowy składnik
                                2. Edytuj składnik
                                3. Usuń składnik
                                0. Powrót
                                """);
                        switch (scanner.nextInt()) {
                            case 1 -> addIngredient();
                            case 2 -> editIngredient();
                            case 3 -> deleteIngredient();
                            case 0 -> back1 = true;
                        }
                    }
                }
                case 2 -> {
                    boolean back2 = false;
                    while (!back2) {
                        System.out.println("""
                                1. Nowy przepis
                                2. Nowy przepis z czasem przygotowania
                                3. Nowy przepis z instrukcją przygotowania
                                4. Nowy przepis z czasem i instrukcją przygotowania
                                5. Edytuj przepis
                                6. Usuń przepis
                                0. Powrót
                                """);
                        switch (scanner.nextInt()) {
                            case 1 -> addRecipe();
                            case 2 -> addPtRecipe();
                            case 3 -> addPiRecipe();
                            case 4 -> addPtPiRecipe();
                            case 5 -> editRecipe();
                            case 6 -> deleteRecipe();
                            case 0 -> back2 = true;
                        }
                    }
                }
                case 3 -> planDay();
                case 4 -> copyPlan();
                case 5 -> showPlan();
                case 6 -> showShoppingList();
                case 7 -> export();
                case 8 -> imprt();
                case 0 -> running = false;
            
      }
    }
    // save appState;
  }

  private static void addIngredient(){
    
  }

  private static void editIngredient(){
    
  }

  private static void deleteIngredient(){
    
  }

  private static void addRecipe(){
    
  }

  private static void addPtRecipe(){
    
  }

  private static void addPiRecipe(){
    
  }

  private static void addPtPiRecipe(){
    
  }

  private static void editRecipe(){
    
  }

  private static void deleteRecipe(){
    
  }

  private static void planDay(){
    
  }

  private static void copyPlan(){
    
  }

  private static void showPlan(){
    
  }
  
  private static void showShoppingList(){
    
  }

  private static void export(){
    
  }

  private static void imprt(){
    
  }
}
