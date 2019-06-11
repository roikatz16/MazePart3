package View;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;



public class MyViewController extends Controller implements IView, Initializable {

    private ArrayList<int[]> solutionAsIntegersList;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    private Button solve;
    @FXML
    private Button restart;

    @FXML
    private Pane MazePane;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Stage s = Main.getStage();
        s.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });
/*
        mazeDisplayer.heightProperty().bind(MazePane.heightProperty());
        mazeDisplayer.widthProperty().bind(MazePane.widthProperty());

        mazeDisplayer.heightProperty().addListener(e->mazeDisplayer.redraw());
        mazeDisplayer.widthProperty().addListener(e->mazeDisplayer.redraw());
*/
    }


    public void KeyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();

    }

    public void solveMaze() throws FileNotFoundException {

        if(solve.getText().equals("Solve")) {
            solve.setText("Hide Solution");

            solve.setDisable(true);
            restart.setDisable(true);
            if(solutionAsIntegersList==null) {
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
        solve.setDisable(true);
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
    private void backToNewGame() throws IOException {
        viewModel.deleteSolution();
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
                if (viewModel.isWon()){
                    //winner();
                }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void winner() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("YOURE THE CHAMPION");
        alert.setHeaderText("DOUZE POINTS! YOU WON THE GAME");
        alert.setContentText("what would you like to do next?");

        ButtonType buttonTypeOne = new ButtonType("Start a new game");
        ButtonType buttonTypeTwo = new ButtonType("Load a previous game");
        ButtonType buttonTypeThree = new ButtonType("Exit");
        ButtonType buttonTypeCancel = new ButtonType("continue playing this game", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            newGame();
        } else if (result.get() == buttonTypeTwo) {
            goToLoadGame();
        } else if (result.get() == buttonTypeThree) {
            exitGame();
        } else {

        }
    }

    public void exitGame() {
        closeProgram();
    }

    @Override
    protected void closeProgram() {
        viewModel.deleteSolution();
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
        loadGame();
    }

    public void properties(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Configuration Properties");
        alert.setHeaderText("Server.ThreadPoolSize = 4\nSolveSearchProblem.SolvingAlgorithm = BestFirstSearch\nGenerateMaze.mazeGeneratorType = MyMazeGenerator");
        alert.showAndWait();


    }
    public void about(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ABOUT US");
        alert.setHeaderText("Roi Katz 308097237 (Single and friendly)\nOmer Hofman 307832972 (Not single and yet friendly)");
        alert.showAndWait();
    }

    public void help(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INSTRUCTIONS");
        alert.setHeaderText("8 = Up\n2 = Down\n4 = Left\n6 = Right\n7 = Up & Left\n9 = Up & Right\n1 = Down & Left\n3 = Down & Right\n");
        alert.showAndWait();
    }



}
