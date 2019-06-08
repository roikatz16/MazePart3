package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class LandingController extends Controller implements Initializable {


    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage s = Main.getStage();

        s.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });
    }


    public Button newGameButton;
    public Button oldGameButton;
    public Button instructionsButton;
    public Button exitButton;

    public void exitGame(ActionEvent actionEvent) {
        closeProgram();
    }

    public void goToInstructions(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Maze game instructions");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("I have a great message for you!");
        alert.showAndWait();

    }

    public void goToNewGame(ActionEvent actionEvent) throws IOException {
        newGame(actionEvent);
    }


    public void oldGamesMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader();
        Stage s = Main.getStage();
        Parent root = fxmlLoader.load(getClass().getResource("../View/Load.fxml").openStream());
        s.setScene(new Scene(root, 600, 400));
        LoadController lc = fxmlLoader.getController();
        lc.setViewModel(viewModel);
        viewModel.addObserver(lc);
        Main.setStage(s);
        s.show();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
