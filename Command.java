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

    private final List<Recipe> recipes = new ArrayList<>();

    public void addRecipe(Recipe recipe)
    {
        recipes.add(recipe);
        System.out.println("Added recipe: " + recipe);
    }

    public void editRecipe(Recipe oldRecipe, Recipe newRecipe)
    {
        int index = recipes.indexOf(oldRecipe);

        if (index != -1)
        {
            recipes.set(index, newRecipe);
            System.out.println("Edited recipe: " + oldRecipe.getName());
        }
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

    public void undo()
    {
        recipeManager.deleteRecipe(recipe);
    }
}

class EditRecipeCommand implements Command
{
    private final RecipeManager recipeManager;
    private final Recipe newRecipe;
    private Recipe oldRecipe;

    public EditRecipeCommand(RecipeManager recipeManager, Recipe newRecipe)
    {
        this.recipeManager = recipeManager;
        this.newRecipe = newRecipe;
    }

    public void execute()
    {
        oldRecipe = null;

        for (Recipe recipe : recipeManager.getRecipes())
        {
            if (recipe.getName().equals(newRecipe.getName()))
            {
                oldRecipe = recipe;
                break;
            }
        }

        if (oldRecipe != null)
        {
            recipeManager.editRecipe(oldRecipe, newRecipe);
        }
    }

    public void undo()
    {
        if (oldRecipe != null)
        {
            recipeManager.editRecipe(newRecipe, oldRecipe);
        }
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

    public void undo()
    {
        recipeManager.addRecipe(recipe);
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