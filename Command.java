import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public interface Command
{
    void execute();
    void undo();
}

class RecipeManager
{
    private final List<IRecipe> recipes = new ArrayList<>();

    public void addRecipe(IRecipe recipe)
    {
        recipes.add(recipe);
        System.out.println("Added recipe: " + recipe);
    }

    public void editRecipe(IRecipe oldRecipe, IRecipe newRecipe)
    {
        int index = recipes.indexOf(oldRecipe);

        if (index != -1)
        {
            recipes.set(index, newRecipe);
            System.out.println("Edited recipe: " + oldRecipe.getName());
        }
    }

    public void deleteRecipe(IRecipe recipe)
    {
        recipes.remove(recipe);
        System.out.println("Deleted recipe: " + recipe);
    }

    public void importRecipes(String filePath, List<Ingredient> IngredientRepository)
    {
        recipes.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(";");

                String name = parts[0];
                int servings = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;

                Recipe.RecipeBuilder builder = new Recipe.RecipeBuilder()
                        .setName(name)
                        .setServings(servings);
                
                if (parts.length > 2 && !parts[2].isEmpty())
                {
                    String[] ingredients = parts[2].split("!");

                    for (String ing : ingredients)
                    {
                        String[] ingParts = ing.split(",");

                        String ingName = ingParts[0];
                        float amount = Float.parseFloat(ingParts[1]);

                        Ingredient ingredient = IngredientRepository.stream()
                                                    .filter(i -> i.getName().equals(ingName))
                                                    .findFirst()
                                                    .orElse(null); 

                        if (ingredient != null)
                        {
                            builder.addIngredient(ingredient, amount);
                        }
                    }
                }

                recipes.add(builder.build());
            }
        }
        catch (IOException e)
        {
            System.err.println("Import failed: " + e.getMessage());
        }
    }

    public void exportRecipes(String filePath)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
        {
            for (IRecipe recipe : recipes)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(recipe.getName())
                    .append(";")
                    .append(recipe.getServings())
                    .append(";");

                if (recipe instanceof Recipe concreteRecipe)
                {
                    boolean first = true;
                    for (var entry : concreteRecipe.getIngredients().entrySet())
                    {
                        if (!first) sb.append("!");
                        first = false;

                        Ingredient ing = entry.getKey();
                        float amount = entry.getValue();

                        sb.append(ing.getName())
                            .append(",")
                            .append(amount);
                    }
                }
                writer.write(sb.toString());
                writer.newLine();
            }
        }
        catch (IOException e)
        {
            System.err.println("Export failed: " + e.getMessage());
        }
    }

    public List<IRecipe> getRecipes()
    {
        return recipes;
    }
 
    public IRecipe getLastRecipe() {
      if (recipes.isEmpty()) return null;
      return recipes.get(recipes.size() - 1);
    }

    public void replaceLastRecipe(IRecipe newRecipe) {
      if (!recipes.isEmpty()) {
        recipes.set(recipes.size() - 1, newRecipe);
      }
    }

}

class AddRecipeCommand implements Command
{
    private final RecipeManager recipeManager;
    private final IRecipe recipe;

    public AddRecipeCommand(RecipeManager recipeManager, IRecipe recipe)
    {
        this.recipeManager = recipeManager;
        this.recipe = recipe;
    }

    public void execute()
    {
        recipeManager.addRecipe(recipe);
    }

    public void undo()
    {
        recipeManager.deleteRecipe(recipe);
    }
}

class EditRecipeCommand implements Command
{
    private final RecipeManager recipeManager;
    private final IRecipe newRecipe;
    private IRecipe oldRecipe;

    public EditRecipeCommand(RecipeManager recipeManager, IRecipe newRecipe, IRecipe oldRecipe)
    {
        this.recipeManager = recipeManager;
        this.newRecipe = newRecipe;
        this.oldRecipe = oldRecipe;
    }

    public void execute()
    {
         recipeManager.editRecipe(oldRecipe, newRecipe);
    }

    public void undo()
    {
        recipeManager.editRecipe(newRecipe, oldRecipe);
    }
}

class DeleteRecipeCommand implements Command
{
    private final RecipeManager recipeManager;
    private final IRecipe recipe;

    public DeleteRecipeCommand(RecipeManager recipeManager, Recipe recipe)
    {
        this.recipeManager = recipeManager;
        this.recipe = recipe;
    }

    public void execute()
    {
        recipeManager.deleteRecipe(recipe);
    }

    public void undo()
    {
        recipeManager.addRecipe(recipe);
    }
}

class ImportRecipesCommand implements Command
{
    private final RecipeManager recipeManager;
    private final String file;
    private final List<Ingredient> IngredientRepository;
    
    public ImportRecipesCommand(RecipeManager recipeManager, String file, List<Ingredient> IngredientRepository)
    {
        this.recipeManager = recipeManager;
        this.file = file;
        this.IngredientRepository = IngredientRepository;
    }

    public void execute()
    {
        recipeManager.importRecipes(file, IngredientRepository);
    }

    public void undo()
    {
       
    }
}

class ExportRecipesCommand implements  Command
{
    private final RecipeManager recipeManager;
    private final String file;

    public ExportRecipesCommand(RecipeManager recipeManager, String file)
    {
        this.recipeManager = recipeManager;
        this.file = file;
    }

    public void execute()
    {
        recipeManager.exportRecipes(file);
    }

    public void undo()
    {
        File exportedFile = new File(file);

        if (exportedFile.exists())
        {
            if (exportedFile.delete())
            {
                System.out.println("Export undone, file deleted: " + file);
            }
            else
            {
                System.err.println("Failed to delete exported file: " + file);
            }
        }
    }
}

class CommandController
{
    private final Stack<Command> history = new Stack<>();
    

    public void executeCommand(Command command)
    {
        command.execute();
        history.push(command);
    }

    public void undoLast(int n)
    {
        for (int i = 0; i < n && !history.isEmpty(); i++)
        {
            Command command = history.pop();
            command.undo();
        }
    }

    public void undo()
    {
        undoLast(1);
    }
}
