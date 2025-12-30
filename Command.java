import java.io.*;
import java.util.ArrayList;
import java.util.List;

public interface Command
{
    void execute();
}

class RecipeManager
{

    private final List<Recipe> recipes = new ArrayList<>();

    public void addRecipe(Recipe recipe)
    {
        recipes.add(recipe);
        System.out.println("Added recipe: " + recipe);
    }

    public void editRecipe(Recipe recipe)
    {
        System.out.println("Edited recipe: " + recipe);
    }

    public void deleteRecipe(Recipe recipe)
    {
        recipes.remove(recipe);
        System.out.println("Deleted recipe: " + recipe);
    }

    public void importRecipes(String filePath)
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

                Recipe recipe = new Recipe.RecipeBuilder()
                        .setName(name)
                        .setServings(servings)
                        .build();
                
                        recipes.add(recipe);
            }
            System.out.println("Recipes imported from: " + filePath);
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
            for (Recipe recipe : recipes)
            {
                writer.write(recipe.getName() + ";" + recipe.getServings());
                writer.newLine();
            }
            System.out.println("Recipes exported to: " + filePath);
        }
        catch (IOException e)
        {
            System.err.println("Export failed: " + e.getMessage());
        }
    }

    public List<Recipe> getRecipes()
    {
        return recipes;
    }
}

class AddRecipeCommand implements Command
{
    private final RecipeManager recipeManager;
    private final Recipe recipe;

    public AddRecipeCommand(RecipeManager recipeManager, Recipe recipe)
    {
        this.recipeManager = recipeManager;
        this.recipe = recipe;
    }

    public void execute()
    {
        recipeManager.addRecipe(recipe);
    }
}

class EditRecipeCommand implements Command
{
    private final RecipeManager recipeManager;
    private final Recipe recipe;

    public EditRecipeCommand(RecipeManager recipeManager, Recipe recipe)
    {
        this.recipeManager = recipeManager;
        this.recipe = recipe;
    }

    public void execute()
    {
        recipeManager.editRecipe(recipe);
    }
}

class DeleteRecipeCommand implements Command
{
    private final RecipeManager recipeManager;
    private final Recipe recipe;

    public DeleteRecipeCommand(RecipeManager recipeManager, Recipe recipe)
    {
        this.recipeManager = recipeManager;
        this.recipe = recipe;
    }

    public void execute()
    {
        recipeManager.deleteRecipe(recipe);
    }
}

class ImportRecipesCommand implements Command
{
    private final RecipeManager recipeManager;
    private final String file;

    public ImportRecipesCommand(RecipeManager recipeManager, String file)
    {
        this.recipeManager = recipeManager;
        this.file = file;
    }

    public void execute()
    {
        recipeManager.importRecipes(file);
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
}

class CommandController
{
    private final List<Command> commandQueue = new ArrayList<>();

    public void addCommand(Command command)
    {
        commandQueue.add(command);
    }

    public void executeCommands()
    {
        for (Command command : commandQueue)
        {
            command.execute();
        }
        commandQueue.clear();
    }
}