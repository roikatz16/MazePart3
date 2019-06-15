package View;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class NewGameController extends Controller implements Initializable{


    String[] gameParams = new String[11];

    public TextField size;
    public Text rowLabel;
    @FXML
    private AnchorPane anchor;
    @FXML
    private AnchorPane anchorGali;
    @FXML
    private AnchorPane anchorDana;
    @FXML
    private AnchorPane anchorIzhar;
    @FXML
    private AnchorPane anchorTrophy;
    @FXML
    private SplitPane leftSplit;
    @FXML
    private SplitPane rightSplit;
    @FXML
    private Label labelDiff;
    @FXML
    private Label labelchar;


    /**
     * initialize the class (mainly for close program properly)
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = Main.getViewModel();
        gameParams[0] = "easy";
        gameParams[3] = "Netta";


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
        // do nothing. needed because the inheriting
    }

    /**
     * close the program with confirming
     * close the servers
     */
    public void exitGame() {
        super.closeProgram();
    }

   /* SCENE-CHANGING METHODS:
    1. goToLanding
    2. goToMainScene
     */
    /**
     * Go to landing scene
     * @throws IOException
     */
    public void backToLanding() throws IOException {
        super.goToLanding();
    }

    /**
     * Go to Main scene: the game scene
     * @throws IOException
     */
    public void StartGame() throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader();
        sendParamsToViewModel();
        Stage stage = Main.getStage();
        super.goToMainScene(fxmlLoader, stage);
        generateMaze();
        Main.setStage(stage);
        stage.show();
    }

    /**
     * send parameters (difficulty, character) to viewModel
     */
    private void sendParamsToViewModel() {
        viewModel.setParams(gameParams);
    }
    /*
        ///////////////MAZE PARAMS/////////////
        0 - DIFFICULTY
        1 - DIFFICULTY (CUSTOM CASE) ROWS
        2 - DIFFICULTY (CUSTOM CASE) COLUMNS
        3 - CHARACTER
        4 - THEME
        4 - TIME (OLD GAME CASE)
        5 - CHARACTER ROW POSITION
        6 - CHARACTER COL POSITION
        7 - CHARACTER START  ROW POSITION
        8 - CHARACTER START COL POSITION
        9 - CHARACTER END  ROW POSITION
        10 - CHARACTER END COL POSITION
    */

    /**
     * Creating the maze (through the model and the servers)
     */
    private void generateMaze() {
        //get maze params from scene
        int row = 0;
        int col = 0;

        switch (gameParams[0]){
            case "easy":
                row = 25;
                col = 25;
                break;
            case "medium":
                row = 37;
                col = 37;
                break;
            case "hard":
                row = 51;
                col = 51;
                break;
            case "custom":
                getCustomMazeSize();
                row = Integer.parseInt(gameParams[1]);
                col = Integer.parseInt(gameParams[2]);
                if(row%2==0){
                    row++;
                    col++;
                }
        }
        viewModel.generateMaze(row, col,gameParams[3]);
    }

    /**
     *
     */
    private void getCustomMazeSize() {
        if (isInt(size,size.getText()) && isInt(size,size.getText()) ){
            gameParams[1] = size.getText();
            gameParams[2] = size.getText();
        }
    }

    /* Pick Difficulty methods */
    public void pickEasy() {
        gameParams[0]="easy";
        size.setDisable(true);
    }
    public void pickMedium() {
        gameParams[0]="medium";
        size.setDisable(true);
    }
    public void PickHard() {
        gameParams[0]="hard";
        size.setDisable(true);
    }

    public void pressedCustom() {
        gameParams[0]="custom";
        size.setDisable(false);
    }

    /* Pick Character methods */

    public void pickNetta() { gameParams[3]="Netta"; }
    public void pickDana() { gameParams[3]="Dana"; }
    public void pickIzhar() {gameParams[3]="Izhar"; }
    public void pickGali() {gameParams[3]="Gali"; }


    /**
     * Verify the player putted valid input in the custom size
     * @param input
     * @param message
     * @return
     */
    private boolean isInt(TextField input, String message){
        try{
            int age = Integer.parseInt(input.getText());
            System.out.println("row/col is: " + age);
            return true;
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Look, an Error Dialog");
            alert.setContentText("Ooops, there was an error! " + message + " is not a number");

            alert.showAndWait();
            return false;
        }
    }



}

