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
    }

    public void editRecipe(Recipe recipe)
    {

    }

    public void deleteRecipe(Recipe recipe)
    {
        recipes.remove(recipe);
    }

    public void importRecipes(String file)
    {

    }

    public void exportRecipes(String file)
    {

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