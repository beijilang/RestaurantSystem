package frontend.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class ManagerController {
    @FXML
    GridPane tableView = new GridPane();
    @FXML
    Button signOut;

    private int myId;
    public void setmyId(int id){
        myId = id;
    }

    @FXML protected void request(ActionEvent event) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        Parent root;
        try {
            FXMLLoader numLoader = new FXMLLoader(this.getClass().getResource("/frontend/GUI/Request.fxml"));
            Parent scene = numLoader.load();
            window.setTitle("Ingredient request!");
            window.setScene(new Scene(scene, 600, 600));
            window.showAndWait();
        } catch (IOException e) {
            System.out.println("request error");
        }

    }

    @FXML protected void ingredientAmount(ActionEvent event) {
        //TODO inventory to string
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        Parent root;
        try {
            FXMLLoader numLoader = new FXMLLoader(this.getClass().getResource("/frontend/GUI/InventoryDisplay.fxml"));
            Parent scene = numLoader.load();
            window.setTitle("Welcome!");
            window.setScene(new Scene(scene, 600, 600));
            window.showAndWait();
        } catch (IOException e) {
            System.out.println("Ingredient amount error");
        }
    }

    @FXML private void logOff() throws IOException {
        FXMLLoader startLoader = new FXMLLoader(this.getClass().getResource("/frontend/GUI/Start.fxml"));
        GridPane root = startLoader.load();
        Scene mainScene = new Scene(root, 600, 600);
        BackgroundImage mainImage = new BackgroundImage(new Image("hp.jpg", 600, 600, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        root.setBackground(new Background(mainImage));

        StartSceneController paneController = startLoader.getController();
        paneController.start();

        Stage window = (Stage)(signOut.getScene().getWindow());

        window.setTitle("Welcome to Four Guys Restaurant System");
        window.setScene(mainScene);
        window.show();
    }
}
