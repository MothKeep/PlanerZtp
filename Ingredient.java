import java.lang;

enum IngredientType{
  DAIRY,
  VEGETABLE,
  FRUIT,
  MEAT,
  GRAIN,
  OTHER
}

public class Ingredient{

  private String name;
  private int calories;
  private float protein;
  private float fat;
  private float carbs;
  private IngredientType type;

  private Ingredient(IngredientBuilder builder){
    this.name = builder.name;
    this.calories = builder.calories;
    this.protein = builder.protein;
    this.fat = builder.fat;
    this.carbs = builder.carbs;
    this.type = builder.type;
  }

  public static class IngredientBuilder{

    private String name;
    private int calories;
    private float protein;
    private float fat;
    private float carbs;
    private IngredientType type;

    public IngredientBuilder setName(String name){
      this.name = name;
      return this;
    }

    public IngredientBuilder setCalories(int calories){
      this.calories = calories;
      return this;
    }

    public IngredientBuilder setProtein(float protein){
      this.protein = protein;
      return this;
    }

    public IngredientBuilder setFat(float fat){
      this.fat = fat;
      return this;
    }

    public IngredientBuilder setCarbs(float carbs){
      this.carbs = carbs;
      return this;
    }

    public IngredientBuilder setType(IngredientType type){
      this.type = type;
      return this;
    }

    public Ingredient build(){
      return new Ingredient(this);
    }
  }

  public String getName(){return name;} ///////////////
  public int getCalories(){return calories;}
  public float getProtein(){return protein;}
  public float getFat(){return fat;}
  public float getCarbs(){return carbs;}

  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append("Nazwa produktu: " + name + "\n");
    sb.append("Kalorie: " + calories.toString() + "\n");
    sb.append("Białka: " + protein.toString() + "\n");
    sb.append("Tłuszcze: " + fat.toString() + "\n");
    sb.append("Węglowodany: " + carbs.toString() + "\n");
    
    String typ;
    switch (type) {
      case DAIRY:
        typ = "Nabiał";
        break;
      case VEGETABLE:
        typ = "Warzywo";
        break;
      case FRUIT:
        typ = "Owoc";
        break;
      case MEAT:
        typ = "Mięso";
        break;
      case GRAIN:
        typ = "Zbożowe";
        break;
      case OTHER:
        typ = "Inne";
        break;
      default:
        break;
    }
    
    sb.append("Typ produktu: " + typ + "\n");
    return sb.toString();
  }
}
