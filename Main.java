//import
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

  public void editIngredient(Scanner scanner) {
    if (ingredients.isEmpty()) {
      System.out.println("Brak składników do edycji");
      return;
    }

    System.out.println("Wybierz składnik do edycji:");
    for (int i = 0; i < ingredients.size(); i++) {
      System.out.println(i + " - " + ingredients.get(i).toString());
    }
    System.out.print("Twój wybór: ");
    int index = scanner.nextInt();
    scanner.nextLine();

    if (index >= 0 && index < ingredients.size()) {
      ingredients.get(index).edit();
    } else {
      System.out.println("Nieprawidłowy wybór składnika!");
    }
  }

  public Ingredient findIngredient(String name) {
    for (Ingredient i : ingredients) {
      if (i.getName().equalsIgnoreCase(name)) {
        return i;
      }
    }
      return null;
  }

  public CommandController getCommandController() {
    return commandController;
  }
}

public class Main{
  private static final AppState appState = new AppState();
  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args){
    Command importRecipes = new ImportRecipesCommand(appState.getRecipeManager(), "ball_Recipe");
    appState.getCommandController().executeCommand(importRecipes);
    IngredientRepository.clear();
    try (BufferedReader reader = new BufferedReader(new FileReader("ball_Ingredient"))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(";");
        if (parts.length < 6)
          continue;

        String name = parts[0];
        int calories = Integer.parseInt(parts[1]);
        float protein = Float.parseFloat(parts[2]);
        float fat = Float.parseFloat(parts[3]);
        float carbs = Float.parseFloat(parts[4]);
        IngredientType type = IngredientType.valueOf(parts[5]);

        Ingredient ingredient = new Ingredient.IngredientBuilder()
                                  .setName(name)
                                  .setCalories(calories)
                                  .setProtein(protein)
                                  .setFat(fat)
                                  .setCarbs(carbs)
                                  .setType(type)
                                  .build();

        appState.addIngredient(ingredient);
      }
    } catch (Exception e) {
      System.out.println("Błąd przy imporcie składników: " + e.getMessage());
    }

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
      scanner.nextLine();
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
                        int ch2 = scanner.nextInt();
                        scanner.nextLine();
                        switch (ch2) {
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
                        int ch2 = scanner.nextInt();
                        scanner.nextLine();
                        switch (ch2){
                            case 1 -> addRecipe();
                            case 2 -> addPtRecipe();
                            case 3 -> addPsRecipe();
                            case 4 -> addPtPsRecipe();
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
 
    Command exportRecipes = new ExportRecipesCommand(appState.getRecipeManager(), "ball_Recipe");
    appState.getCommandController().executeCommand(exportRecipes);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("ball_Ingredient"))) {
      for (Ingredient ingredient : appState.getIngredients()) {
        writer.write(ingredient.getName() + ";" + ingredient.getCalories() + ";" + ingredient.getProtein() + 
          ";" + ingredient.getFat() + ";" + ingredient.getCarbs() + ";" + ingredientTypeToString(ingredient));
        writer.newLine();
      }
    } catch (Exception e) {
      System.out.println("Błąd przy eksporcie składników: " + e.getMessage());
    }
  } 
  private static void addIngredient(){
    Ingredient.IngredientBuilder iB = new Ingredient.IngredientBuilder();
    
    System.out.print("Podaj nazwę składnika: ");
    String name = scanner.nextLine();
    iB.setName(name);

    System.out.print("Podaj kalorie (int): ");
    int calories = scanner.nextInt();
    iB.setCalories(calories);
    scanner.nextLine();

    System.out.print("Podaj białka (float): ");
    float protein = scanner.nextFloat();
    iB.setProtein(protein);
    scanner.nextLine();

    System.out.print("Podaj tłuszcze (float): ");
    float fat = scanner.nextFloat();
    iB.setFat(fat);
    scanner.nextLine();

    System.out.print("Podaj węglowodany (float): ");
    float carbs = scanner.nextFloat();
    iB.setCarbs(carbs);
    scanner.nextLine();
    
    System.out.println("Wybierz typ składnika:");
    for (IngredientType t : IngredientType.values()) {
      System.out.println(t.ordinal() + " - " + t);
    }
    int typeIndex = scanner.nextInt();
    scanner.nextLine(); 
    iB.setType(IngredientType.values()[typeIndex]);

    Ingredient i = iB.build();
    appState.addIngredient(i);
  }

  private static void editIngredient(){
    appState.editIngredient(scanner);   
  }

  private static void deleteIngredient() {
    List<Ingredient> ingredients = appState.getIngredients();

    if (ingredients.isEmpty()) {
      System.out.println("Brak składników do usunięcia!");
      return;
    }

    System.out.println("Wybierz składnik do usunięcia:");
    for (int i = 0; i < ingredients.size(); i++) {
      System.out.println(i + " - " + ingredients.get(i).getName());
    }

    System.out.print("Twój wybór (numer składnika): ");
    int index = scanner.nextInt();
    scanner.nextLine(); 

    if (index >= 0 && index < ingredients.size()) {
      Ingredient toRemove = ingredients.get(index);
      appState.removeIngredient(toRemove);
      System.out.println("Usunięto składnik: " + toRemove.getName());
    } else {
      System.out.println("Nieprawidłowy wybór składnika!");
    }
  }
  
  private static void addRecipe() {
    scanner.nextLine(); 
    System.out.print("Podaj nazwę przepisu: ");
    String name = scanner.nextLine();

    Recipe.RecipeBuilder builder = new Recipe.RecipeBuilder();
    builder.setName(name);

    boolean addingIngredients = true;
    while (addingIngredients) {
      System.out.println("Dodaj składnik do przepisu:");
      for (int i = 0; i < appState.getIngredients().size(); i++) {
        System.out.println(i + " - " + appState.getIngredients().get(i).getName());
      }
      System.out.print("Wybierz indeks składnika (-1 aby zakończyć): ");
      int index = scanner.nextInt();
      scanner.nextLine();
      if (index == -1) break;

      if (index >= 0 && index < appState.getIngredients().size()) {
        Ingredient ingredient = appState.getIngredients().get(index);
        System.out.print("Podaj ilość: ");
        int amount = scanner.nextInt();
        scanner.nextLine();
        builder.addIngredient(ingredient, amount);
      } else {
        System.out.println("Nieprawidłowy wybór!");
      }
    }

    IRecipe recipe = builder.build();
    appState.getRecipeManager().addRecipe(recipe);
    System.out.println("Dodano przepis: " + recipe.getName());
  }

  private static void addPtRecipe() {
    addRecipe();
    IRecipe lastRecipe = appState.getRecipeManager().getLastRecipe(); 

    System.out.print("Podaj czas przygotowania w minutach: ");
    int time = scanner.nextInt();
    scanner.nextLine();

    IRecipe decorated = new PreparationTimeDecorator(lastRecipe, time);
    appState.getRecipeManager().replaceLastRecipe(decorated);

    System.out.println("Dodano czas przygotowania: " + time + " minut");
  }

  private static void addPsRecipe() {
    addRecipe();
    IRecipe lastRecipe = appState.getRecipeManager().getLastRecipe();

    scanner.nextLine(); 
    List<String> steps = new ArrayList<>();
    boolean addingSteps = true;
    while (addingSteps) {
      System.out.print("Dodaj krok (-1 aby zakończyć): ");
      String step = scanner.nextLine();
      if (step.equals("-1")) break;
      steps.add(step);
    }

    IRecipe decorated = new PreparationStepsDecorator(lastRecipe, steps);
    appState.getRecipeManager().replaceLastRecipe(decorated);

    System.out.println("Dodano kroki przygotowania: " + steps.size());
  }

  private static void addPtPsRecipe() {
    addRecipe();
    IRecipe lastRecipe = appState.getRecipeManager().getLastRecipe();

    System.out.print("Podaj czas przygotowania w minutach: ");
    int time = scanner.nextInt();
    scanner.nextLine(); 

    List<String> steps = new ArrayList<>();
    boolean addingSteps = true;
    while (addingSteps) {
      System.out.print("Dodaj krok (-1 aby zakończyć): ");
      String step = scanner.nextLine();
      if (step.equals("-1")) break;
      steps.add(step);
    }

    IRecipe decorated = new PreparationStepsDecorator(new PreparationTimeDecorator(lastRecipe, time), steps);
    appState.getRecipeManager().replaceLastRecipe(decorated);

    System.out.println("Dodano przepis z czasem przygotowania i krokami.");
  }

  private static void editRecipe() {
    List<IRecipe> recipes = appState.getRecipeManager().getRecipes();

    if (recipes.isEmpty()) {
      System.out.println("Brak dostępnych przepisów do edycji.");
      return;
    }

    System.out.println("Dostępne przepisy:");
    for (int i = 0; i < recipes.size(); i++) {
      System.out.println((i + 1) + ". " + recipes.get(i).getName());
    }

    System.out.println("Wybierz przepis do edycji (numer):");
    int choice;
    try {
      choice = Integer.parseInt(scanner.nextLine());
    } catch (Exception e) {
      System.out.println("Niepoprawny numer.");
      return;
    }

    if (choice < 1 || choice > recipes.size()) {
      System.out.println("Niepoprawny numer.");
      return;
    }

    IRecipe oldRecipe = recipes.get(choice - 1);

    System.out.println("Nowa nazwa przepisu (pozostaw puste, aby nie zmieniać):");
    String newName = scanner.nextLine();
    if (newName.isBlank())
      newName = oldRecipe.getName();

    Recipe.RecipeBuilder builder = new Recipe.RecipeBuilder()
      .setName(newName)
      .setServings(oldRecipe.getServings());

    for (Map.Entry<Ingredient, Integer> entry : oldRecipe.getIngredients().entrySet()) {
      builder.addIngredient(entry.getKey(), entry.getValue());
    }

    boolean editingIngredients = true;
    while (editingIngredients) {
      System.out.println("""
        1. Dodaj składnik
        2. Usuń składnik
        0. Zakończ edycję składników
      """);

      int ingChoice;
      try {
        ingChoice = Integer.parseInt(scanner.nextLine());
      } catch (Exception e) {
        System.out.println("Niepoprawny wybór.");
        continue;
      }

      switch (ingChoice) {
        case 1 -> {
          List<Ingredient> ingredients = appState.getIngredients();
          if (ingredients.isEmpty()) {
            System.out.println("Brak dostępnych składników.");
            continue;
          }
          System.out.println("Dostępne składniki:");
          for (int i = 0; i < ingredients.size(); i++) {
            System.out.println((i + 1) + ". " + ingredients.get(i).getName());
          }
          System.out.println("Wybierz składnik (numer):");
          int ingNum;
          try {
            ingNum = Integer.parseInt(scanner.nextLine());
          } catch (Exception e) {
            System.out.println("Niepoprawny numer.");
            continue;
          }
          if (ingNum < 1 || ingNum > ingredients.size()) {
            System.out.println("Niepoprawny numer.");
            continue;
          }
          Ingredient selectedIngredient = ingredients.get(ingNum - 1);
          System.out.println("Podaj ilość składnika:");
          int amount;
          try {
            amount = Integer.parseInt(scanner.nextLine());
          } catch (Exception e) {
            System.out.println("Niepoprawna ilość.");
            continue;
          }
            builder.addIngredient(selectedIngredient, amount);
          }
          case 2 -> {
            List<Ingredient> currentIngredients = new ArrayList<>(builder.build().getIngredients().keySet());
            if (currentIngredients.isEmpty()) {
              System.out.println("Brak składników do usunięcia.");
              continue;
            }
            System.out.println("Aktualne składniki:");
            for (int i = 0; i < currentIngredients.size(); i++) {
              System.out.println((i + 1) + ". " + currentIngredients.get(i).getName());
            }
            System.out.println("Wybierz składnik do usunięcia (numer):");
            int delNum;
            try {
              delNum = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
              System.out.println("Niepoprawny numer.");
              continue;
            }
            if (delNum < 1 || delNum > currentIngredients.size()) {
            System.out.println("Niepoprawny numer.");
            continue;
          }
          Ingredient toRemove = currentIngredients.get(delNum - 1);
          Map<Ingredient, Integer> ingMap = new HashMap<>(builder.build().getIngredients());
          ingMap.remove(toRemove);
          builder = new Recipe.RecipeBuilder()
            .setName(builder.build().getName())
            .setServings(builder.build().getServings());
          ingMap.forEach(builder::addIngredient);
        }
        case 0 -> editingIngredients = false;
        default -> System.out.println("Niepoprawny wybór.");
      }
    }

    Recipe newRecipe = builder.build();
    EditRecipeCommand command = new EditRecipeCommand(appState.getRecipeManager(), newRecipe, oldRecipe);
    appState.getCommandController().executeCommand(command);
    System.out.println("Przepis zaktualizowany.");
  }

  private static void deleteRecipe() {
    List<IRecipe> recipes = appState.getRecipeManager().getRecipes();

    if (recipes.isEmpty()) {
      System.out.println("Brak przepisów do usunięcia.");
      return;
    }

    System.out.println("Dostępne przepisy:");
    for (int i = 0; i < recipes.size(); i++) {
      System.out.println((i + 1) + ". " + recipes.get(i).getName());
    }

    System.out.println("Wybierz przepis do usunięcia (numer):");
    int choice;
    try {
      choice = Integer.parseInt(scanner.nextLine());
    } catch (Exception e) {
      System.out.println("Niepoprawny numer.");
      return;
    }

    if (choice < 1 || choice > recipes.size()) {
      System.out.println("Niepoprawny numer.");
      return;
    }

    Recipe recipeToDelete = (Recipe) recipes.get(choice - 1);
    DeleteRecipeCommand command = new DeleteRecipeCommand(appState.getRecipeManager(), recipeToDelete);
    appState.getCommandController().executeCommand(command);
    System.out.println("Przepis usunięty.");
  }

  private static void planDay() {
    System.out.println("Podaj datę dnia, który chcesz zaplanować (rrrr-mm-dd):");
    String dateStr = scanner.nextLine();
    LocalDate date;
    try {
      date = LocalDate.parse(dateStr);
    } catch (Exception e) {
      System.out.println("Niepoprawny format daty.");
      return;
    }

    DayPlan dayPlan = null;
    for (MealComponent component : appState.getMealPlan().getComponents()) {
      if (component instanceof DayPlan dp && dp.getDate().equals(date)) {
        dayPlan = dp;
        break;
      }
    }

    if (dayPlan == null) {
      dayPlan = new DayPlan(date);
      appState.getMealPlan().addComponent(dayPlan);
    }

    boolean addingMeals = true;
    while (addingMeals) {
      System.out.println("""
        Wybierz typ posiłku:  
        1. Śniadanie
        2. Drugie śniadanie
        3. Obiad
        4. Przekąska
        5. Kolacja
        0. Zakończ dodawanie posiłków
      """);

      int choice;
      try {
        choice = Integer.parseInt(scanner.nextLine());
      } catch (Exception e) {
        System.out.println("Niepoprawny wybór.");
        continue;
      }

      MealType mealType;
      switch (choice) {
        case 1 -> mealType = MealType.BREAKFAST;
        case 2 -> mealType = MealType.LUNCH;
        case 3 -> mealType = MealType.DINNER;
        case 4 -> mealType = MealType.SNACK;
        case 5 -> mealType = MealType.SUPPER;
        case 0 -> {
          addingMeals = false;
          continue;
        }
        default -> {
          System.out.println("Niepoprawny wybór.");
          continue;
        }
      }
      List<IRecipe> recipes = appState.getRecipeManager().getRecipes();
      if (recipes.isEmpty()) {
        System.out.println("Brak dostępnych przepisów.");
        continue;
      }

      System.out.println("Dostępne przepisy:");
      for (int i = 0; i < recipes.size(); i++) {
        System.out.println((i + 1) + ". " + recipes.get(i).getName());
      }

      System.out.println("Wybierz przepis (numer):");
      int recipeChoice;
      try {
        recipeChoice = Integer.parseInt(scanner.nextLine());
      } catch (Exception e) {
        System.out.println("Niepoprawny numer.");
        continue;
      }

      if (recipeChoice < 1 || recipeChoice > recipes.size()) {
        System.out.println("Niepoprawny numer.");
        continue;
      }

      IRecipe selectedRecipe = recipes.get(recipeChoice - 1);
      System.out.println("Podaj godzinę posiłku (np. 08:00):");
      String time = scanner.nextLine();

      dayPlan.addMeal(time, new Meal(selectedRecipe, mealType));
      System.out.println("Posiłek dodany.");
    }
  }

  private static void copyPlan() {
    System.out.println("Podaj datę dnia, który chcesz skopiować (rrrr-mm-dd):");
    String sourceDateStr = scanner.nextLine();
    LocalDate sourceDate;
    try {
      sourceDate = LocalDate.parse(sourceDateStr);
    } catch (Exception e) {
      System.out.println("Niepoprawny format daty.");
      return;
    }

    DayPlan sourceDay = null;
    for (MealComponent component : appState.getMealPlan().getComponents()) {
      if (component instanceof DayPlan dayPlan && dayPlan.getDate().equals(sourceDate)) {
        sourceDay = dayPlan;
        break;
      }
    }

    if (sourceDay == null) {
      System.out.println("Nie znaleziono planu dla podanej daty.");
      return;
    }

    System.out.println("Podaj datę docelową, na którą chcesz skopiować plan (rrrr-mm-dd):");
    String targetDateStr = scanner.nextLine();
    LocalDate targetDate;
    try {
      targetDate = LocalDate.parse(targetDateStr);
    } catch (Exception e) {
      System.out.println("Niepoprawny format daty.");
      return;
    }

    DayPlan newDay = (DayPlan) sourceDay.clone();
    try {
      java.lang.reflect.Field dateField = DayPlan.class.getDeclaredField("date");
      dateField.setAccessible(true);
      dateField.set(newDay, targetDate);
    } catch (Exception e) {
      System.out.println("Błąd podczas ustawiania daty docelowej.");
      return;
    }

    appState.getMealPlan().addComponent(newDay);
    System.out.println("Plan skopiowany na " + targetDate);
  }

  private static void showShoppingList() {
    MealPlan mealPlan = appState.getMealPlan();

    if (mealPlan.getComponents().isEmpty()) {
      System.out.println("Plan posiłków jest pusty.");
      return;
    }

    Map<IngredientType, Map<String, Integer>> shoppingList = new HashMap<>();

    for (MealComponent component : mealPlan.getComponents()) {
      if (component instanceof DayPlan dayPlan) {
        for (Meal meal : dayPlan.getMeals().values()) {
          IRecipe recipe = meal.getRecipe();
          int servings = recipe.getServings();

          for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredients().entrySet()) {
            Ingredient ingredient = entry.getKey();
            int amount = entry.getValue() * servings;

            shoppingList
              .computeIfAbsent(ingredient.getType(), k -> new HashMap<>())
              .merge(ingredient.getName(), amount, Integer::sum);
          }
        }
      }
    }
    if (shoppingList.isEmpty()) {
      System.out.println("Brak składników w planie.");
      return;
    }

    System.out.println("\nLista zakupów dla całego planu:");

    for (IngredientType type : IngredientType.values()) {
      Map<String, Integer> items = shoppingList.get(type);
      if (items != null && !items.isEmpty()) {
        String typeName;
        switch (type) {
          case DAIRY: typeName = "Nabiał"; break;
          case VEGETABLE: typeName = "Warzywa"; break;
          case FRUIT: typeName = "Owoce"; break;
          case MEAT: typeName = "Mięso"; break;
          case GRAIN: typeName = "Zbożowe"; break;
          case OTHER: typeName = "Inne"; break;
          default: typeName = "Inne"; break;
        }
        System.out.println(typeName + ":");

        items.forEach((name, amount) -> System.out.println("- " + name + ": " + amount));
        System.out.println();
      }
    }
  }
private static void showPlan() {
    MealPlan mealPlan = appState.getMealPlan();
    List<MealComponent> components = mealPlan.getComponents();

    if (components.isEmpty()) {
        System.out.println("Plan posiłków jest pusty.");
        return;
    }

    for (MealComponent component : components) {
        if (component instanceof DayPlan dayPlan) {
            System.out.println(dayPlan.toString(""));
        }
    }
}
  private static void export() {
    System.out.print("Podaj ścieżkę do pliku eksportu przepisów: ");
    String recipesFile = scanner.nextLine();

    Command exportRecipes = new ExportRecipesCommand(appState.getRecipeManager(), recipesFile);
    appState.getCommandController().executeCommand(exportRecipes);

    System.out.print("Podaj ścieżkę do pliku eksportu składników: ");
    String ingredientsFile = scanner.nextLine();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(ingredientsFile))) {
      for (Ingredient ingredient : appState.getIngredients()) {
        writer.write(ingredient.getName() + ";" + ingredient.getCalories() + ";" + ingredient.getProtein() + ";" + ingredient.getFat() + ";" + ingredient.getCarbs() + ";" + ingredientTypeToString(ingredient));
        writer.newLine();
      }
      System.out.println("Składniki wyeksportowane do: " + ingredientsFile);
    } catch (Exception e) {
      System.out.println("Błąd przy eksporcie składników: " + e.getMessage());
    }
  }

  private static String ingredientTypeToString(Ingredient ingredient) {
    try {
      java.lang.reflect.Field typeField = Ingredient.class.getDeclaredField("type");
      typeField.setAccessible(true);
      Object type = typeField.get(ingredient);
    return type.toString();
    } catch (Exception e) {
      return "OTHER";
    }
  }
  private static void imprt() {
    System.out.print("Podaj ścieżkę do pliku importu przepisów: ");
    String ingredientsFile = scanner.nextLine();
    IngredientRepository.clear();

    try (BufferedReader reader = new BufferedReader(new FileReader(ingredientsFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(";");
        if (parts.length < 6)
          continue;

        String name = parts[0];
        int calories = Integer.parseInt(parts[1]);
        float protein = Float.parseFloat(parts[2]);
        float fat = Float.parseFloat(parts[3]);
        float carbs = Float.parseFloat(parts[4]);
        IngredientType type = IngredientType.valueOf(parts[5]);

        Ingredient ingredient = new Ingredient.IngredientBuilder()
                                  .setName(name)
                                  .setCalories(calories)
                                  .setProtein(protein)
                                  .setFat(fat)
                                  .setCarbs(carbs)
                                  .setType(type)
                                  .build();

        appState.addIngredient(ingredient);
        IngredientRepository.register(ingredient);
      }
      System.out.println("Składniki zaimportowane z: " + ingredientsFile);
    } catch (Exception e) {
        System.out.println("Błąd przy imporcie składników: " + e.getMessage());
    }
    System.out.print("Podaj ścieżkę do pliku importu składników: ");
    String recipesFile = scanner.nextLine();

    Command importRecipes = new ImportRecipesCommand(appState.getRecipeManager(), recipesFile);
    appState.getCommandController().executeCommand(importRecipes);
  }
}
