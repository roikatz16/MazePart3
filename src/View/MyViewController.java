package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.Main;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class MyViewController extends Controller implements IView, Initializable {

    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    private Button solve;
    @FXML
    private Button restart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage s = Main.getStage();
        s.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });


    }

    public void KeyPressed(KeyEvent keyEvent) {
        //   isMakingMove = true;
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();

    }

    public void solveMaze(ActionEvent actionEvent) throws InterruptedException {
        solve.setDisable(true);
        solve.cancelButtonProperty();
        restart.setDisable(true);
        restart.cancelButtonProperty();
       TimeUnit.SECONDS.sleep(2);
        viewModel.solveMaze();
        solve.setDisable(false);
        restart.setDisable(false);
    }

    public void restart() throws FileNotFoundException {
        restart.setDisable(true);
        //restart.cancelButtonProperty();
        solve.setDisable(true);
        //solve.cancelButtonProperty();
        int characterPositionRow = viewModel.getRowStartPosition();
        int characterPositionColumn = viewModel.getColStartPosition();
        viewModel.setRowCurrentPosition(characterPositionRow);
        viewModel.setColCurrentPosition(characterPositionColumn);
        mazeDisplayer.setCharacterPosition(viewModel.getRowCurrentPosition(), viewModel.getColCurrentPosition());
        restart.setDisable(false);
        solve.setDisable(false);
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {

        if (o == viewModel) {

            try {
                int goalPositionRow = viewModel.getRowGoalPosition();
                int goalPositionCol = viewModel.getColGoalPosition();
                mazeDisplayer.setMaze(viewModel.getMazeAsArray(), goalPositionRow, goalPositionCol );
                int characterPositionRow = viewModel.getRowCurrentPosition();
                int characterPositionColumn = viewModel.getColCurrentPosition();
                mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
                if(arg!="move"){
                    mazeDisplayer.setSolutionAsIntegersList(viewModel.getSolutionAsIntegersList());
                }



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void exitGame(ActionEvent actionEvent) {
        closeProgram();
    }

    private void closeProgram() {
        Stage s = Main.getStage();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save the game?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            s.close();
            viewModel.close();
        }
    }

    public void saveGame(ActionEvent actionEvent) {


        TextInputDialog dialog = new TextInputDialog("my game");
        dialog.setTitle("SAVE GAME");
        dialog.setHeaderText("Please, save your game");
        dialog.setContentText("Please enter a valid name (characters only!):");

        String fileName ="";
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            fileName = result.get();
        }

        int CharacterPositionRow = mazeDisplayer.getCharacterPositionRow();
        int characterPositionCol = mazeDisplayer.getCharacterPositionColumn();
        String gameParams[] = mazeDisplayer.getGameParams();
        String characterName = gameParams[3];

        viewModel.saveGame(CharacterPositionRow, characterPositionCol,characterName,fileName);
    }
}
