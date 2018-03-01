package inventory;

import java.util.HashMap;
        import table.Table;

public class Dish {
    private String name;
//    private HashMap<Ingredient, Integer> ingredients;
    private HashMap<String, Integer> IngredientsInventory = new HashMap<>();
    private int cost;
//    private int dishNumber;
    private boolean isReady;
    Table table;

    public Dish(Table t, String name){
        this.name = name;
        table = t;
        isReady = false;
        ingredients = new HashMap<Ingredient, Integer>();
    }

    public void adjustIngredient(Ingredient in, int amount){
        ingredients.put(in,ingredients.get(in)+amount);
    }

    public void ready(){
        isReady = true;
    }

    public void recook(){
        isReady = false;
    }

    public boolean isReady(){
        return isReady;
    }

    public String getName(){
        return name;
    }

    public int getCost() {
        return cost;
    }

    public Table getTable() {
        return table;
    }

    public String toString(){
        return name +"    "+ cost;
    }
}
