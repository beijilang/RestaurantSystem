package frontend.GUI;

import backend.inventory.*;
import backend.server.Packet;
import backend.table.Order;
import frontend.client.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuController{
    @FXML
    GridPane tableView = new GridPane();
    private Stage window;
    public Client client = Client.getInstance();
    int numoforder = 0;
    volatile HashMap<String, InventoryIngredient> defaultInventory = (HashMap<String, InventoryIngredient>) client.sendRequest(Packet.REQUESTINVENTORY); //TODO should get menu from web ComputerServer requestMenu()
    public Inventory inventory = Inventory.getInstance();
    private ArrayList<Dish> recipe = new ArrayList<>();
    private int myId;


    volatile HashMap<String, DishRecipe> menuDishes = (HashMap<String, DishRecipe>) client.sendRequest(Packet.REQUESTMENU);
    Menu menu = Menu.getInstance();

    Order dishOrder = new Order();
    HashMap<String,Dish> order = new HashMap<>();

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public void setStage(Stage stage){
        this.window = stage;
    }


    public void updateInventory(ArrayList changed) {
        for(Object in: changed){
            InventoryIngredient inventoryIngredient = (InventoryIngredient) in;
            String name = inventoryIngredient.getName();
            InventoryIngredient ingredient = inventory.getIngredient(name);
            ingredient.setQuantity(inventoryIngredient.getMirrorQuantity());
        }
        updateMenu();
    }

    public void updateMenu() {
        for(Dish dish:recipe){
            boolean cookable = dish.ableToCook(inventory);
            System.out.println(dish.getName()+" "+cookable);
            Button tb = (Button) tableView.lookup("#"+dish.getName());
            if(cookable){
                tb.setDisable(false);
            }
            else{
                tb.setDisable(true);
            }
        }
        try{
            window.showAndWait();
        }catch(IllegalStateException e) { }
    }




    public void initialize() throws IOException{
        menu.setDishes(menuDishes);
        inventory.setStock(defaultInventory);
        HashMap<String,DishRecipe> dishes = menu.getDishes();
        //set up dishes
        int i = 0;
        for(String di: dishes.keySet()){
            Button item = new Button(di);
            item.setId(di);
            item.setOnAction(new EventHandler<ActionEvent>() {
                //order a dish
                //TODO update inventory and order updateMenu()
                @Override public void handle(ActionEvent e) {
                   Button ordered = new Button(di);
                   ordered.setId(""+numoforder);

                   //set up the ingredient adjustment interface
                   Stage st = new Stage();
                   Dish dish = new Dish(dishes.get(di));

                   //pass ingredient to server
                   HashMap<String,DishIngredient> ingredients = dish.getIngredientsRequired();
                   ArrayList<DishIngredient> dishIngredients = new ArrayList<>();
                   for(String in: ingredients.keySet()){
                       dishIngredients.add(ingredients.get(in));
                   }
                   client.sendAdjustIngredientRequest(dishIngredients, true);

                   order.put(""+numoforder, dish);
                   dishOrder.addDish(dish);
                   FXMLLoader ingredientLoader = new FXMLLoader(this.getClass().getResource("/frontend/GUI/Ingredient.fxml"));
                    try {
                        GridPane ingredient = ingredientLoader.load();
                        IngredientController controller = ingredientLoader.getController();
                        controller.getDish(dish);
                        Scene ingredientScene = new Scene(ingredient, 400, 400);
                        st.setScene(ingredientScene);
                        st.show();
                    } catch (IOException e1) {
                        System.out.println(ingredientLoader);
                    }


                    ordered.setOnAction(new EventHandler<ActionEvent>() {
                        //delete a dish
                       //TODO update order and inventory updateMenu()
                        @Override public void handle(ActionEvent e) {
                            tableView.getChildren().remove(ordered);
                            //dishOrder.remove(ordered);
                        }
                    });

                   numoforder++;
                   tableView.add(ordered,2,numoforder);
                }
            });

            tableView.add(item,0,i);
            i++;


        }
        TextField tableNum = new TextField();
        tableNum.maxWidth(30);
        tableView.add(tableNum,0,5);

        //submit the order
        Button submit = new Button("order");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //TODO receive the order, sendOrder()
                ((Stage) submit.getScene().getWindow()).close();
            }
        });
        tableView.add(submit,1,5);

        //set up background
        BackgroundImage menuImage= new BackgroundImage(new Image("menu.jpg",600,600,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background= new Background(menuImage);
        tableView.setBackground(background);


        for(String di: dishes.keySet()){
            recipe.add(new Dish(dishes.get(di)));
        }
    }


}
