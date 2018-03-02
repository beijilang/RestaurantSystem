package inventory;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Menu {
    private HashMap<String, Dish> dishes = new HashMap<>();
    private Inventory inventory;

    public Menu(Inventory inventory)throws IOException{
        this.inventory = inventory;
        create();
    }

    public void create()throws IOException{
        try (BufferedReader fileReader = new BufferedReader(new FileReader("phase1/menu.txt"))) {

            // Print the lines from f prefaced wiZZth the line number,
            // starting at 1.
            String line = fileReader.readLine();
            while (line != null) {
                String[] items = line.split(";");
                String name = items[0];
                int price = Integer.parseInt(items[1]);
                String[] ingredients = items[2].split(",");
                dishes.put(name, new Dish(name,price, ingredients, inventory));
                line = fileReader.readLine();
                }

            }
    }

    public Dish getDish(String name){
        return dishes.get(name);
    }





}
