import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface IRecipe{

  void addIngredient(Ingredient ingredient, Float amount);
  void removeIngredient(Ingredient ingredient);
  void recalculateNutrition();
  String getName();
  int getTotalCalories();
  float getTotalProtein();
  float getTotalFat();
  float getTotalCarbs();
  int getServings();
  Map<Ingredient, Float> getIngredients();
}

public class Recipe implements IRecipe{
  
  private String name;
  private Map<Ingredient, Float> ingredients;
  private int servings;
  private int totalCalories;
  private float totalProtein;
  private float totalFat;
  private float totalCarbs;

  private Recipe(RecipeBuilder builder){
    this.name = builder.name;
    this.ingredients = builder.ingredients;
    this.servings = builder.servings;
    recalculateNutrition();
  }

  @Override
  public void addIngredient(Ingredient ingredient, Float amount){
    ingredients.put(ingredient, amount);
    recalculateNutrition();
  }

  @Override
  public void removeIngredient(Ingredient ingredient){
    ingredients.remove(ingredient);
    recalculateNutrition();
  }

  @Override
  public void recalculateNutrition(){
    totalCalories = 0;
    totalProtein = 0;
    totalFat = 0;
    totalCarbs = 0;

    for(Map.Entry<Ingredient, Float> entry : ingredients.entrySet()){
      Ingredient ingredient = entry.getKey();
      Float amount = entry.getValue();

      totalCalories += ingredient.getCalories() * amount;
      totalProtein += ingredient.getProtein() * amount;
      totalFat += ingredient.getFat() * amount;
      totalCarbs += ingredient.getCarbs() * amount;
    }

    if(servings > 1){
      totalCalories /= servings;
      totalProtein /= servings;
      totalFat /= servings;
      totalCarbs /= servings;
    }
  }

  @Override
  public String getName() {return name;}
  
  @Override
  public int getTotalCalories() {return totalCalories;}

  @Override
  public float getTotalProtein() {return totalProtein;}

  @Override 
  public float getTotalFat() {return totalFat;}

  @Override
  public float getTotalCarbs() {return totalCarbs;}

  @Override 
  public int getServings() {return servings;}

///////////////////////// Ksawier tycał
  @Override
  public Map<Ingredient, Float> getIngredients() {
    return Collections.unmodifiableMap(ingredients);
  }
///////////////////////////

  public static class RecipeBuilder{
    private String name;
    private Map<Ingredient, Float> ingredients = new HashMap<>();
    private int servings = 1;

    public RecipeBuilder setName(String name){
      this.name = name;
      return this;
    }

    public RecipeBuilder addIngredient(Ingredient ingredient, Float amount){
      ingredients.put(ingredient, amount);
      return this;
    }

    public RecipeBuilder setServings(int servings){
      this.servings = servings;
      return this;
    }

    public Recipe build(){
      return new Recipe(this);
    }
  }
}

abstract class RecipeDecorator implements IRecipe{
  protected IRecipe wrappedRecipe;

  public RecipeDecorator(IRecipe recipe){
    this.wrappedRecipe = recipe;
  }

  @Override
  public void addIngredient(Ingredient ingredient, Float amount){
    wrappedRecipe.addIngredient(ingredient, amount);
  }

  @Override
  public void removeIngredient(Ingredient ingredient){
    wrappedRecipe.removeIngredient(ingredient);
  }

  @Override
  public void recalculateNutrition(){
    wrappedRecipe.recalculateNutrition();
  }

  @Override
  public String getName() {return wrappedRecipe.getName();}

  @Override
  public int getTotalCalories() {return wrappedRecipe.getTotalCalories();}

  @Override
  public float getTotalProtein() {return wrappedRecipe.getTotalProtein();}

  @Override
  public float getTotalFat() {return wrappedRecipe.getTotalFat();}

  @Override
  public float getTotalCarbs() {return wrappedRecipe.getTotalCarbs();}

  @Override
  public int getServings() {return wrappedRecipe.getServings();}

  @Override
  public Map<Ingredient, Float> getIngredients() {
    return wrappedRecipe.getIngredients();
  }
///////////////////////////////
}

class PreparationTimeDecorator extends RecipeDecorator{
  private int preparationTime;

  public PreparationTimeDecorator(IRecipe recipe, int preparationTime){
    super(recipe);
    this.preparationTime = preparationTime;
  }

  public int getPreparationTime() {return preparationTime;}
}

class PreparationStepsDecorator extends RecipeDecorator{
  private List<String> steps;

  public PreparationStepsDecorator(IRecipe recipe, List<String> steps){
    super(recipe);
    this.steps = steps;
  }

  public List<String> getSteps() {return steps;}
}
