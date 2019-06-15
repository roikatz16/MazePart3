package View;


import Server.Configurations;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
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

    /*
    METHODS:
    1. initialize, update
    2. maze events: key-pressed (move character), solve, restart, win
    3. menu events: new, load, save, exit
     */

    /**
     * initialize the class (mainly for close program properly)
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Stage s = Main.getStage();
        s.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });

        /*mazeDisplayer.heightProperty().bind(MazePane.heightProperty());
        mazeDisplayer.widthProperty().bind(MazePane.widthProperty());

        mazeDisplayer.heightProperty().addListener(e->mazeDisplayer.redraw());
        mazeDisplayer.widthProperty().addListener(e->mazeDisplayer.redraw());*/

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
                if (viewModel.isWon()){
                    winner();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * MAZE EVENTS:
     * 1. KeyPressed
     * 2. solveMaze
     * 3. restart
     * 4. winner
     */

    /**
     * move the character (if able to)
     * @param keyEvent
     */
    public void KeyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();

    }

    /**
     * Solve the maze if it is not solved yet
     * show the solution / hide the solution
     * @throws FileNotFoundException
     */
    public void solveMaze() {

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

    /**
     * Move the character to the start of the maze
     * @throws FileNotFoundException
     */
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

    /**
     * In case of winning:
     * 1. cogrates the player
     * 2. open an alert for 'what to do next'
     * @throws IOException
     */
    private void winner() throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("YOURE THE CHAMPION");
        alert.setHeaderText("DOUZE POINTS! YOU WON THE GAME");
        alert.setContentText("what would you like to do next?");

        ButtonType buttonTypeOne = new ButtonType("Start a new game");
        ButtonType buttonTypeTwo = new ButtonType("Load a previous game");
        ButtonType buttonTypeThree = new ButtonType("Exit");
        ButtonType buttonTypeCancel = new ButtonType("continue playing this game");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            goToNewGame();
        } else if (result.get() == buttonTypeTwo) {
            goToLoadGame();
        } else if (result.get() == buttonTypeThree) {
            exitGame();
        }

    }


    /*
     * MAZE MENU EVENTS:
     * 1. New
     * 2. Load
     * 3. Save
     * 4. properties / help / about
     * 5. Exit
     */

    /**
     * going to "new game" scene
     * @throws IOException
     */
    @FXML
    private void backToNewGame() throws IOException {
        viewModel.deleteSolution();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save the game before starting a new one?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        // alert.se
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            saveGame();
            super.goToNewGame();
        }

        else if(alert.getResult() == ButtonType.NO){
            super.goToNewGame();
        }
    }


    /**
     * save the game
     */
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

    /**
     * load new game --> loadGame(father's method)
     * @throws IOException
     */
    public void loadGame() throws IOException {
        super.goToLoadGame();
    }

    /**
     * show Configuration Properties (Configuration file // part B)
     */
    public void properties(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Configuration Properties");
        String ThreadPoolSize = Configurations.getProperty("Server.ThreadPoolSize");
        String algorithm = Configurations.getProperty("SolveSearchProblem.SolvingAlgorithm");
        String generate = Configurations.getProperty("GenerateMaze.mazeGeneratorType");
        alert.setHeaderText("Server's Thread Pool Size = "+ThreadPoolSize+"\nSolving Algorithm = "+algorithm+"\nMaze Generator Type = "+generate);
        alert.showAndWait();
    }

    /**
     * Show information About us
     */
    public void about(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ABOUT US");
        alert.setHeaderText("Roi Katz 308097237 (Single and friendly)\nOmer Hofman 307832972 (Not single and yet friendly)");
        alert.showAndWait();
    }

    /**
     * Show instructions
     */
    public void help(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INSTRUCTIONS");
        alert.setHeaderText("8 = Up\n2 = Down\n4 = Left\n6 = Right\n7 = Up & Left\n9 = Up & Right\n1 = Down & Left\n3 = Down & Right\n");
        alert.showAndWait();
    }
    /**
     * exit game --> closeProgram
     */
    public void exitGame() {
        this.closeProgram();
    }

    /**
     * exit the game with saving offer
     * close the servers
     */
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

    /**
     * resize maze displayer method
     * @param scene
     */

    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {

                mazeDisplayer.resize(mazeDisplayer.getWidth(),(double) newSceneWidth);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {

                mazeDisplayer.resize((double) newSceneHeight,mazeDisplayer.getHeight());
            }
        });
    }


    /**
     * ZOOM IN METHOD
     * @param event zoom in
     */
    public void handleScroll(ScrollEvent event) {
        //System.out.println(mazeDisplayer.getScaleX());
        if (event.isControlDown()) return;
        if (mazeDisplayer.getScaleX() <= 3 && mazeDisplayer.getScaleX() >= 0.25) {
            // System.out.println(mazeDisplayer.getScaleX());
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0) {
                zoomFactor = 2.0 - zoomFactor;
            }

            mazeDisplayer.setScaleX(mazeDisplayer.getScaleX() * zoomFactor);
            mazeDisplayer.setScaleY(mazeDisplayer.getScaleY() * zoomFactor);
        } else if (mazeDisplayer.getScaleX() > 3) {
            mazeDisplayer.setScaleX(3);
            mazeDisplayer.setScaleY(3);
        } else if (mazeDisplayer.getScaleX() < 0.25) {
            mazeDisplayer.setScaleX(0.25);
            mazeDisplayer.setScaleY(0.25);
        }
        event.consume();
    }


}
