package inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Inventory {
    private static HashMap<String, Ingredient> ingredientsInventory;



    // read off the menu and just set arbitrary value for each ingredient
    public Inventory() {
        ingredientsInventory = new HashMap<>();
    }


    public Ingredient getIngredient(String ingredientName){
        return ingredientsInventory.get(ingredientName);
    }

    public void add(Ingredient ingredient){
        ingredientsInventory.put(ingredient.getName(),ingredient);
    }


//    public void addQuantity(Ingredient receivedIngredient) {
//        String ingredientName = dishIngredient.getName();
//        int dishIngredientQuantity = dishIngredient.getQuantity();
//        int ingredientStock = ingredientsInventory.get(ingredientName).getQuantity();
//
//        int updatedQuantity = ingredientStock + dishIngredientQuantity;
//
//        ingredientsInventory.get(ingredientName).setQuantity(updatedQuantity);
//    }
//
//    public void reduceQuantity(Ingredient dishIngredient) {
//        String ingredientName = dishIngredient.getName();
//        int dishIngredientQuantity = dishIngredient.getQuantity();
//        int ingredientStock = ingredientsInventory.get(ingredientName).getQuantity();
//
//        int updatedQuantity = ingredientStock - dishIngredientQuantity ;
//
//        ingredientsInventory.get(ingredientName).setQuantity(updatedQuantity);
//    }


}
