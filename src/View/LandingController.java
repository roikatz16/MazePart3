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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
        newGame();
    }


    public void oldGamesMenu(ActionEvent actionEvent) throws IOException {
        //oldGame(actionEvent);

        //validation();
        FXMLLoader fxmlLoader1 = new FXMLLoader();
        Stage d = Main.getStage();
        Parent root = fxmlLoader1.load(getClass().getResource("../View/Load.fxml").openStream());
        d.setScene(new Scene(root,900,600));
        LoadController lc = fxmlLoader1.getController();
        lc.setViewModel(viewModel);

        Main.setStage(d);
        d.show();
    }

    @Override
    public void update(Observable o, Object arg) {

    }


    private void validation() {
        ArrayList<String> savedFiles = getTitlesOfFiles();
        if (savedFiles.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Error");
            alert.setHeaderText("Ooops, there was an error!");
            alert.setContentText("you have not saved any file, please create new game first!");
            alert.showAndWait();
        }
    }

}
