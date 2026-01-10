import java.util.Scanner;

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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) 
      return true;
    if (!(obj instanceof Ingredient)) 
      return false;
    Ingredient that = (Ingredient) obj;
    return name.equalsIgnoreCase(that.name);
  }
  
  @Override
  public int hashCode() {
    return name.toLowerCase().hashCode();
  }

  public String getName(){return name;} 
  public int getCalories(){return calories;}
  public float getProtein(){return protein;}
  public float getFat(){return fat;}
  public float getCarbs(){return carbs;}
  public IngredientType getType(){return type;}

  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append("Nazwa produktu: " + name + "\n");
    sb.append("Kalorie: " + calories + "\n");
    sb.append("Białka: " + protein + "\n");
    sb.append("Tłuszcze: " + fat + "\n");
    sb.append("Węglowodany: " + carbs + "\n");
    
    String typ="";
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

  public void edit(){
      Scanner scanner = new Scanner(System.in);
      boolean editing = true;

      while (editing) {
        System.out.println("\nCo chcesz zmienić?");
        System.out.println("1 - Nazwa");
        System.out.println("2 - Kalorie");
        System.out.println("3 - Białko");
        System.out.println("4 - Tłuszcze");
        System.out.println("5 - Węglowodany");
        System.out.println("6 - Typ");
        System.out.println("0 - Zakończ edycję");
        System.out.print("Twój wybór: ");

        int choice = scanner.nextInt();
        scanner.nextLine();  

        switch (choice) {
          case 1 -> {
            System.out.print("Nowa nazwa: ");
            this.name = scanner.nextLine();
          }
          case 2 -> {
            System.out.print("Nowa liczba kalorii: ");
            this.calories = scanner.nextInt();
          }
          case 3 -> {
            System.out.print("Nowa ilość białka: ");
            this.protein = scanner.nextFloat();
          }
          case 4 -> {
            System.out.print("Nowa ilość tłuszczu: ");
            this.fat = scanner.nextFloat();
          }
          case 5 -> {
            System.out.print("Nowa ilość węglowodanów: ");
            this.carbs = scanner.nextFloat();
          }
          case 6 -> {
            System.out.println("Wybierz typ składnika:");
            for (IngredientType t : IngredientType.values()) {
              System.out.println(t.ordinal() + " - " + t);
            }
            int typeChoice = scanner.nextInt();
            if (typeChoice >= 0 &&
            typeChoice < IngredientType.values().length) {
              this.type = IngredientType.values()[typeChoice];
            } else {
              System.out.println("Nieprawidłowy wybór typu");
            }
          }
          case 0 -> editing = false;
          default -> System.out.println("Nieprawidłowa opcja");
        }
      }

    }
}
