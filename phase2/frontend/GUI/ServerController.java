package frontend.GUI;

import backend.employee.ServiceEmployee;
import backend.inventory.Dish;
import backend.server.Packet;
import frontend.client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public class ServerController implements Initializable {
  private Scene menuScene;

  private final Color COLOR_OCCUPIED = Color.BLUE;
  private final Color COLOR_EMPTY = Color.WHITE;

  private Client client = Client.getInstance();

  @FXML private VBox gridParent;
  @FXML private HBox hBox1;
  @FXML private HBox hBox2;
  @FXML private TableView finishedDishTableView;
  @FXML private GridPane tableView = new GridPane();

  private ArrayList<Rectangle> rectangleArrayList = new ArrayList<>();
  private int selectedTableNumber;
  private int numOfTables;
  private int myId;

  public void setMyId(int id) {
    this.myId = id;
  }

  public void setMenuScene(Scene scene) {
    menuScene = scene;
  }

  // TODO: In backend, this method should be called to update after each time a dish is finished by
  // cook
  public void updateTableView() {
    ObservableList<Dish> dishes = FXCollections.observableArrayList();

    LinkedList<Dish> dishesCompleted =
        (LinkedList<Dish>) ServiceEmployee.getOrderQueue().getDishesCompleted();

    dishes.addAll(dishesCompleted);
    finishedDishTableView.setItems(dishes);
  }

  public void updateTableView(LinkedList<Dish> dishesCompleted) {
    ObservableList<Dish> dishes = FXCollections.observableArrayList();

    dishes.addAll(dishesCompleted);
    finishedDishTableView.setItems(dishes);
  }

  // TODO: In backend, this method should be called to update after each time takeSeat is called by
  // any server.
  // TODO: In backend, this method should be called to update after each time takeSeat is called by
  // any server.
  public void updateTableColor(int tableNumber, boolean occupied) {
    if (occupied) {
      rectangleArrayList.get(tableNumber - 1).setFill(COLOR_OCCUPIED);
    } else {
      rectangleArrayList.get(tableNumber - 1).setFill(COLOR_EMPTY);
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // set up background
    BackgroundImage menuImage =
        new BackgroundImage(
            new Image("server.png", 600, 600, false, true),
            BackgroundRepeat.REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
    Background background = new Background(menuImage);
    int size = 15;
    Rectangle rec1 = new Rectangle(size, size);
    Label label1 = new Label(" : table is occupied");
    Rectangle rec2 = new Rectangle(size, size);
    Label label2 = new Label(" : table is empty");

    rec1.setFill(COLOR_OCCUPIED);
    rec1.setStroke(Color.BLACK);
    rec2.setFill(COLOR_EMPTY);
    rec2.setStroke(Color.BLACK);

    hBox1.getChildren().addAll(rec1, label1);
    hBox2.getChildren().addAll(rec2, label2);
    hBox1.setPadding(new Insets(10, 0, 0, 10));
    hBox2.setPadding(new Insets(10, 0, 0, 10));

    GridPane tableGrid = new GridPane();
    tableGrid.setHgap(10);
    tableGrid.setVgap(8);
    tableGrid.setPadding(new Insets(40, 0, 0, 40));

    // Retrieve number of tables from backend.
    System.out.println("Requesting table");
    numOfTables = (Integer) client.sendRequest(Packet.REQUESTNUMBEROFTABLES);
    int sideLength = (int) ceil(sqrt(numOfTables));

    for (int i = 0; i < numOfTables; i++) {
      Rectangle r = new Rectangle(50, 50);
      r.setFill(COLOR_EMPTY);
      r.setStroke(Color.BLACK);
      Label l = new Label(Integer.toString(i + 1));
      rectangleArrayList.add(r);

      StackPane stackPane = new StackPane();
      stackPane.getChildren().add(r);
      stackPane.getChildren().add(l);
      stackPane.setOnMouseClicked(
          e -> {
            for (Rectangle rectangle : rectangleArrayList) {
              rectangle.setStrokeWidth(1);
              rectangle.setStroke(Color.BLACK);
            }
            r.setStroke(Color.LIGHTBLUE);
            r.setStrokeWidth(3);
            selectedTableNumber = Integer.parseInt(l.getText());
          });

      GridPane.setConstraints(stackPane, i % sideLength, i / sideLength);
      tableGrid.getChildren().add(stackPane);
    }

    gridParent.getChildren().add(tableGrid);

    tableView.setBackground(background);
  }

  @FXML
  protected void takeSeat() {

    if (this.selectedTableNumber != 0
        && rectangleArrayList.get(selectedTableNumber - 1).getFill() != COLOR_OCCUPIED) {
      Stage window = new Stage();
      window.initModality(Modality.APPLICATION_MODAL);
      try {
        FXMLLoader numLoader =
            new FXMLLoader(this.getClass().getResource("/frontend/GUI/TakeSeatAlertBox.fxml"));
        Parent scene = numLoader.load();
        window.setTitle("Take Seat");
        window.setScene(new Scene(scene, 300, 200));
        TakeSeatController paneController = numLoader.getController();

        paneController.tableNumber = selectedTableNumber;
        paneController.rectangleArrayList = rectangleArrayList;

        paneController.setTableNumber(selectedTableNumber);
        paneController.setMyId(myId);

        paneController.start();
        window.setMinWidth(300);
        window.setMinHeight(200);
        window.showAndWait();
      } catch (IOException e) {
        System.out.println("take seat error");
      }
    }
  }

  @FXML
  protected void order() {
    if (selectedTableNumber != 0
        && rectangleArrayList.get(selectedTableNumber - 1).getFill() == COLOR_OCCUPIED
        && finishedDishTableView.getItems().isEmpty()) {
      Stage window = new Stage();
      window.initModality(Modality.APPLICATION_MODAL);
      MenuController menuController = (MenuController) client.getStored("menuController");
      menuController.setStage(window);

      // menuController has same id as this controller.
      menuController.setTableNumber(selectedTableNumber);

      window.setTitle("Order Food");
      window.setScene(menuScene);
      window.showAndWait();
    }
  }

  @FXML
  protected void bill() {
    if (selectedTableNumber != 0
        && rectangleArrayList.get(selectedTableNumber - 1).getFill() == COLOR_OCCUPIED) {
      Stage window = new Stage();
      window.initModality(Modality.APPLICATION_MODAL);

      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/GUI/PrintBill.fxml"));
        Parent root = loader.load();
        window.setTitle("Print Bill Station");
        window.setScene(new Scene(root, 600, 600));

        PrintBillController controller = loader.getController();
        controller.tableNumberLabel.setText(Integer.toString(selectedTableNumber));
        controller.setTableNumber(this.selectedTableNumber);

        window.showAndWait();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  protected void deliverSuccessful() {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);

    try {
      FXMLLoader numLoader =
          new FXMLLoader(
              this.getClass().getResource("/frontend/GUI/DeliverSuccessfulAlertBox.fxml"));
      Parent scene = numLoader.load();
      window.setTitle("Dish delivered successfully");
      window.setScene(new Scene(scene, 300, 200));
      DeliverSuccessfulController paneController = numLoader.getController();

      // takeSeatController has same id as this controller.
      paneController.setTableNumber(selectedTableNumber);
      paneController.setMyId(myId);

      paneController.start();
      window.setMinWidth(300);
      window.setMinHeight(200);
      window.showAndWait();
    } catch (IOException e) {
      System.out.println("Deliver successful error");
    }
  }

  @FXML
  protected void deliverFailed() {
    Stage window = new Stage();
    window.initModality(Modality.APPLICATION_MODAL);

    try {
      FXMLLoader numLoader =
          new FXMLLoader(this.getClass().getResource("/frontend/GUI/DeliverFailedAlertBox.fxml"));
      Parent scene = numLoader.load();
      window.setTitle("Dish deliver failed");
      window.setScene(new Scene(scene, 300, 200));
      DeliverFailedController paneController = numLoader.getController();

      // takeSeatController has same id as this controller.
      paneController.setTableNumber(selectedTableNumber);
      paneController.setMyId(myId);

      paneController.start();
      window.setMinWidth(300);
      window.setMinHeight(200);
      window.showAndWait();
    } catch (IOException e) {
      System.out.println("Deliver failed error");
    }
  }

  @FXML
  protected void clear() {
    if (this.selectedTableNumber != 0
        && rectangleArrayList.get(selectedTableNumber - 1).getFill() == COLOR_OCCUPIED) {
      Stage window = new Stage();
      window.initModality(Modality.APPLICATION_MODAL);

      try {
        FXMLLoader loader =
            new FXMLLoader(this.getClass().getResource("/frontend/GUI/ClearTableAlertBox.fxml"));
        Parent root = loader.load();
        window.setScene(new Scene(root, 300, 200));
        ClearTableController controller = loader.getController();

        controller.setTableNumber(this.selectedTableNumber);
        controller.setMyId(myId);
        controller.setText(
            "Are you sure to clear table number " + Integer.toString(selectedTableNumber) + "?");
        controller.rectangleArrayList = this.rectangleArrayList;

        window.setTitle("Clearing table");
        window.setMinWidth(300);
        window.setMinHeight(200);
        window.showAndWait();
      } catch (IOException e) {
        System.out.println("clear table error");
      }
    }
  }

  @FXML
  private void successButtonClicked() {
    Dish dish;
    int dishNumber;
    if (finishedDishTableView.getSelectionModel().getSelectedItem() != null) {
      dish = (Dish) finishedDishTableView.getSelectionModel().getSelectedItem();
      dishNumber = dish.getDishNumber();

      client.sendEvent(Packet.DELIVERDISHCOMPLETED, dishNumber);
    }
  }

  @FXML
  private void failButtonClicked() {
    Dish dish;
    int dishNumber;
    if (finishedDishTableView.getSelectionModel().getSelectedItem() != null) {
      dish = (Dish) finishedDishTableView.getSelectionModel().getSelectedItem();
      dishNumber = dish.getDishNumber();

      client.sendEvent(Packet.DELIVERDISHFAILED, dishNumber);
    }
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
  protected void logOff() throws IOException {
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

    Stage window = (Stage) (hBox1.getScene().getWindow());

    window.setTitle("Welcome to Four Guys Restaurant System");
    window.setScene(mainScene);
    window.show();
  }
}
