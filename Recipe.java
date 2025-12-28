import java.util.Map;
import java.util.HashMap;
import java.util.List;

interface IRecipe{

  void addIngredient(Ingredient ingredient, int amount);
  void removeIngredient(Ingredient ingredient);
  void recalculateNutrition();
  String getName();
  int getTotalCalories();
  float getTotalProtein();
  float getTotalFat();
  float getTotalCarbs();
  int getServings();
}

public class Recipe implements IRecipe{
  
  private String name;
  private Map<Ingredient, Integer> ingredients;
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
  public void addIngredient(Ingredient ingredient, int amount){
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
    //tu  macie coś zrobić
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

  public static class RecipeBuilder{
    private String name;
    private Map<Ingredient, Integer> ingredients = new HashMap<>();
    private int servings = 1;

    public RecipeBuilder setName(String name){
      this.name = name;
      return this;
    }

    public RecipeBuilder addIngredient(Ingredient ingredient, int amount){
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
  public void addIngredient(Ingredient ingredient, int amount){
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
