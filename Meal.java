import java.util.*;

enum MealType{
  BREAKFAST,
  LUNCH,
  DINNER,
  SNACK,
  SUPPER
}

class Meal{
  private final IRecipe recipe;
  private final MealType mealType;

  public Meal(IRecipe recipe, MealType mealType){
    if(recipe == null)
      throw new IllegalArgumentException("Recipe cannot be null");
        
    if(mealType == null)
      throw new IllegalArgumentException("MealType cannot be null");
    
    this.recipe = recipe;
    this.mealType = mealType;
  }

  public IRecipe getRecipe(){
    return recipe;
  }

  public MealType getMealType(){
    return mealType;
  }
}

interface MealComponent extends Cloneable{
  void recalculateNutrition();

  int getTotalCalories();
  float getTotalProtein();
  float getTotalFat();
  float getTotalCarbs();

  void attach(Observer observer);
  void detach(Observer observer);
  void notifyObservers();

  MealComponent clone();
}

class DayPlan implements MealComponent{
  private Date date;
  private Map<String, Meal> meals = new HashMap<>();

  private int totalCalories;
  private float totalProtein;
  private float totalFat;
  private float totalCarbs;

  private List<Observer> observers = new ArrayList<>();

  public DayPlan(Date date){
    this.date = date;
  }
  //==================================================
  public void addMeal(String time, Meal meal){
    meals.put(time,meal);
    recalculateNutrition();
    notifyObservers();
  }

  public void removeMeal(String time){
    meals.remove(time);
    recalculateNutrition();
    notifyObservers();
  }

  public Map<String,Meal> getMeals(){
    return Collections.unmodifiableMap(meals);
  }

  public Date getDate(){
    return date;
  }
  //==================================================
  @Override
  public void recalculateNutrition(){
    totalCalories = 0;
    totalProtein = 0;
    totalFat = 0;
    totalCarbs = 0;

    for(Meal meal : meals.values()){
      IRecipe recipe = meal.getRecipe();
      totalCalories += recipe.getTotalCalories();
      totalProtein += recipe.getTotalProtein();
      totalFat += recipe.getTotalFat();
      totalCarbs += recipe.getTotalCarbs();
    }
  }
  
  @Override
  public int getTotalCalories(){return totalCalories;}

  @Override
  public float getTotalProtein(){return totalProtein;}

  @Override
  public float getTotalFat(){return totalFat;}
 
  @Override
  public float getTotalCarbs(){return totalCarbs;}
  //==================================================
  
  @Override
  public void attach(Observer observer){observers.add(observer);}

  @Override
  public void detach(Observer observer){observers.remove(observer);}

  @Override
  public void notifyObservers(){
    for(Observer observer : observers){
      observer.update(); //Tomaszek 
    }
  } 
  //==================================================
  @Override
  public MealComponent clone(){
    DayPlan copy = new DayPlan((Date) date.clone());

    for(Map.Entry<String,Meal> entry : meals.entrySet()){
      copy.meals.put(entry.getKey(), entry.getValue());
    }

    copy.recalculateNutrition();
    return copy;
  }
} 

class MealPlan implements MealComponent{
  private List<MealComponent> components = new ArrayList<>();

  private int totalCalories;
  private float totalProtein;
  private float totalFat;
  private float totalCarbs;

  private List<Observer> observers = new ArrayList<>();
  //==================================================
  
  public void addComponent(MealComponent component){
    components.add(component);
    recalculateNutrition();
    notifyObservers();
  }

  public void removeComponent(MealComponent component){
    components.remove(component);
    recalculateNutrition();
    notifyObservers();
  }

  public List<MealComponent> getComponents(){
    return Collections.unmodifiableList(components);
  }
  //==================================================

  @Override
  public void recalculateNutrition(){
    totalCalories = 0;
    totalProtein = 0;
    totalFat = 0;
    totalCarbs = 0;

    for(MealComponent component : components){
      component.recalculateNutrition();

      totalCalories += component.getTotalCalories();
      totalProtein += component.getTotalProtein();
      totalFat += component.getTotalFat();
      totalCarbs += component.getTotalCarbs();
    }
  }

  @Override
  public int getTotalCalories(){return totalCalories;}

  @Override
  public float getTotalProtein(){return totalProtein;}
 
  @Override
  public float getTotalFat(){return totalFat;}
  
  @Override
  public float getTotalCarbs(){return totalCarbs;}
  //==================================================
  
  @Override
  public void attach(Observer observer){observers.add(observer);}

  @Override
  public void detach(Observer observer){observers.remove(observer);}

  @Override
  public void notifyObservers(){
    for(Observer observer : observers){
      observer.update();
    }
  }
  //==================================================
  
  @Override
  public MealComponent clone(){
    MealPlan copy = new MealPlan();
    
    for(MealComponent component : components){
      copy.components.add(component.clone());
    }

    copy.recalculateNutrition();
    return copy;
  }
}
