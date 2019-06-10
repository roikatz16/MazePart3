package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class MyViewController extends Controller implements IView, Initializable {

    private ArrayList<int[]> solutionAsIntegersList;
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
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();

    }

    public void solveMaze() throws InterruptedException, FileNotFoundException {

        if(solve.getText().equals("Solve")) {
            solve.setText("Hide Solution");

            solve.setDisable(true);
            restart.setDisable(true);
            if(solutionAsIntegersList==null) {
                TimeUnit.SECONDS.sleep(2);
                viewModel.solveMaze();
            }
            else{
                mazeDisplayer.redrawWithSolution();
            }
            solve.setDisable(false);
            restart.setDisable(false);
        }

        else{
            solve.setText("Solve");
            solve.setDisable(true);
            restart.setDisable(true);
            mazeDisplayer.redrawWithoutSolution();
            solve.setDisable(false);
            restart.setDisable(false);
        }

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

    @FXML
    private void backToNewGame(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save the game before starting a new one?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        // alert.se
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            saveGame();
            newGame();
        }

        else if(alert.getResult() == ButtonType.NO){
            newGame();
        }
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
                mazeDisplayer.setCharacter(viewModel.getParams()[3]);
                int characterPositionRow = viewModel.getRowCurrentPosition();
                int characterPositionColumn = viewModel.getColCurrentPosition();
                mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
                mazeDisplayer.setMaze(viewModel.getMazeAsArray(), goalPositionRow, goalPositionCol );
                solutionAsIntegersList=viewModel.getSolutionAsIntegersList();
                mazeDisplayer.setSolutionAsIntegersList(solutionAsIntegersList);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void exitGame() {
        closeProgram();
    }

    @Override
    protected void closeProgram() {
        Stage s = Main.getStage();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save the game?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES || alert.getResult() == ButtonType.NO) {
            if(alert.getResult() == ButtonType.YES){
                saveGame();
            }
            s.close();
            viewModel.close();
        }
    }

    public void saveGame() {
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

    public void goToLoadGame() throws IOException {
        FXMLLoader fxmlLoader1 = new FXMLLoader();
        saveGame();
        Stage d = Main.getStage();
        Parent root = fxmlLoader1.load(getClass().getResource("../View/Load.fxml").openStream());
        d.setScene(new Scene(root,900,600));
        LoadController lc = fxmlLoader1.getController();
        lc.setViewModel(viewModel);

        Main.setStage(d);
        d.show();
    }
}
