package frontend.GUI;

import backend.inventory.*;
import backend.table.Order;
import frontend.client.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;

public class MenuController{
    @FXML
    GridPane tableView = new GridPane();
    public Client client = Client.getInstance();
    int numoforder = 0;
    //volatile Inventory inventory = (Inventory) client.request("inventory");
    volatile Menu menu = (Menu)client.request("menu"); //TODO should get menu from web ComputerServer requestMenu()
    //private Menu menu = Menu.getInstance();
    Order dishOrder = new Order();
    private Scene serverScene;

    public void updateMenu(ArrayList<InventoryIngredient> changed){
        for(InventoryIngredient in: changed){
            String name = in.getName();
            InventoryIngredient ingredient = Inventory.getIngredient(name);
            //ingredient.
        }
    }


    public void setServerScene(Scene scene) {
        serverScene = scene;
    }


    public void initialize() throws IOException{
        HashMap<String,DefaultDish> dishes = menu.getDishes();
        //set up dishes
        int i = 0;
        for(String di: dishes.keySet()){
            Button item = new Button(di);
            item.setId(di);
            item.setOnAction(new EventHandler<ActionEvent>() {
                //order a dish
                //TODO update inventory and order updateMenu()
                @Override public void handle(ActionEvent e) {
                    item.setDisable(true);
                   Button ordered = new Button(di);
                   ordered.setId(""+numoforder);

                   //set up the ingredient adjustment interface
                   Stage st = new Stage();
                   Dish dish = new Dish(dishes.get(di));
                   HashMap<String,DishIngredient> ingredients = dish.getIngredientsRequired();
                   ArrayList<DishIngredient> dishIngredients = new ArrayList<>();
                   for(String in: ingredients.keySet()){
                       dishIngredients.add(ingredients.get(in));
                   }
                   //client.adjustIngredient(dishIngredients, true);
                   dishOrder.addDish(dish);
                   FXMLLoader ingredientLoader = new FXMLLoader(this.getClass().getResource("/frontend/GUI/Ingredient.fxml"));
                    try {
                        GridPane ingredient = ingredientLoader.load();
                        IngredientController controller = ingredientLoader.getController();
                        controller.getDish(new Dish(dish));
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
                Stage primaryStage = (Stage)((Node)e.getSource()).getScene().getWindow();
                primaryStage.setScene(serverScene);
            }
        });
        tableView.add(submit,1,5);

        //set up background
        BackgroundImage menuImage= new BackgroundImage(new Image("menu.jpg",600,600,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background= new Background(menuImage);
        tableView.setBackground(background);

    }


}
