package View;


import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

public class LandingController extends Controller implements Initializable {

    public Button newGameButton;
    public Button oldGameButton;
    public Button instructionsButton;
    public Button exitButton;

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

    /**
     * update
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        //// do nothing. needed because the inheriting
    }

    /**
     * close the program with confirming
     * close the servers
     */
    public void exitGame() {
        super.closeProgram();
    }

    /**
     * Go to new-game scene
     * @throws IOException
     */
    public void newGame() throws IOException {
        super.goToNewGame();
    }

    /**
     * o to loading scene
     * @throws IOException
     */
    public void oldGamesMenu() throws IOException {
        super.goToLoadGame();
        //validation();

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

    /**
     * show instructions
     */
    public void goToInstructions(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Maze game instructions");
        alert.setHeaderText("Look, an Information Dialog");
        alert.setContentText("I have a great message for you!");
        alert.showAndWait();
    }
}
