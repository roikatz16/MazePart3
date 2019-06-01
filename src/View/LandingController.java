package View;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LandingController {


    public Button newGameButton;
    public Button oldGameButton;
    public Button instructionsButton;
    public Button exitButton;
    Stage stage = (Stage) exitButton.getScene().getWindow();



    public void exitGame(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

    }
}
