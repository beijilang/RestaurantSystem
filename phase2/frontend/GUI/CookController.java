package frontend.GUI;

import backend.inventory.Dish;
import backend.server.Packet;
import backend.table.Order;
import frontend.client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

public class CookController {
  @FXML HBox hBox;
  @FXML Button getOrderButton;
  @FXML Label numOfOrderLabel;
  private int myId;
  /* table view of dishes in progress */
  @FXML private TableView<Dish> tableViewDishesInProgress = new TableView<>();
  @FXML private TableColumn<Dish, String> nameColumn1;
  @FXML private TableColumn<Dish, Integer> numberColumn1;

  /* table view of dishes to be cooked */
  @FXML private TableView<Dish> tableViewDishesInQueue = new TableView<>();
  @FXML private TableColumn<Dish, String> nameColumn2;
  @FXML private TableColumn<Dish, Integer> numberColumn2;
  private int numOfOrdersInQueue;

  private Client client = Client.getInstance();

  public void setmyId(int id) {
    myId = id;
  }

  // TODO: In backend, this method should be called to update after server enters menu, or finishedButton is clicked or getOrderButton is clicked.
  public void updateDishesOnTableView() {
    tableViewDishesInProgress.setItems(getDishesInProgress());
    tableViewDishesInQueue.setItems(getDishesInFirstOrderQueue());
  }

  @FXML
  private void initialize() {
    updateDishesOnTableView();
  }

  private ObservableList<Dish> getDishesInProgress() {
    ObservableList<Dish> dishes = FXCollections.observableArrayList();

    LinkedList<Dish> dishesInProgress =
        (LinkedList<Dish>) client.sendRequest(Packet.REQUESTDISHESINPROGRESS);

    dishes.addAll(dishesInProgress);

    return dishes;
  }

  private ObservableList<Dish> getDishesInFirstOrderQueue() {
    ObservableList<Dish> dishes = FXCollections.observableArrayList();

    LinkedList<Order> ordersInQueue =
        (LinkedList<Order>) client.sendRequest(Packet.REQUESTORDERSINQUEUE);

    numOfOrdersInQueue = ordersInQueue.size();
    numOfOrderLabel.setText("Number of orders in queue: " + Integer.toString(numOfOrdersInQueue));

    if (!ordersInQueue.isEmpty()) {
      dishes.addAll(ordersInQueue.get(0).getDishes());
    }

    return dishes;
  }

  @FXML
  private void getOrderButtonClicked() {
    client.sendEvent(Packet.ORDERRECEIVED);
    /* (cookObject).orderReceived(); */
    /* ------------------------------------------------------------------------------------------ */

    updateDishesOnTableView();
  }

  @FXML
  private void finishedButtonClicked() {
    Dish selectedDish = tableViewDishesInProgress.getSelectionModel().getSelectedItem();

    if (selectedDish != null) {
      int dishNumber = selectedDish.getDishNumber();
      client.sendEvent(Packet.DISHREADY, dishNumber);
      updateDishesOnTableView();
    }

    /* (cookObject).dishReady(dishNumber); */
    /* ------------------------------------------------------------------------------------------ */


  }

  @FXML
  private void receiveItem() {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);

    try {
      FXMLLoader loader =
          new FXMLLoader(this.getClass().getResource("/frontend/GUI/ReceiveItem.fxml"));
      Parent root = loader.load();
      window.setTitle("Receive Item");
      window.setScene(new Scene(root, 400, 200));
      ReceiveItemController controller = loader.getController();
      controller.setMyId(myId);
      controller.start();
      window.showAndWait();
    } catch (IOException e) {
      System.out.println("receive item error");
    }
  }

  @FXML
  private void logOff() throws IOException {
    FXMLLoader startLoader =
        new FXMLLoader(this.getClass().getResource("/frontend/GUI/Start.fxml"));
    GridPane root = startLoader.load();
    Scene mainScene = new Scene(root, 600, 600);
    BackgroundImage mainImage =
        new BackgroundImage(
            new Image("hp.jpg", 600, 600, false, true),
            BackgroundRepeat.REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
    root.setBackground(new Background(mainImage));

    StartSceneController paneController = startLoader.getController();
    paneController.start();

    Stage window = (Stage) (hBox.getScene().getWindow());

    window.setTitle("Welcome to Four Guys Restaurant System");
    window.setScene(mainScene);
    window.show();
  }
}
