package View;


import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.SQLOutput;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements Observer, IView, Initializable {


    @FXML
    private MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    String[] mazeParams;
    char[][] maze;

    /*
    ///////////////MAZE PARAMS/////////////
    0 - DIFFICULTY
    1 - DIFFICULTY (CUSTOM CASE)
    2 -
    2 - CHARACTER
    3 - THEME
    4 - TIME (OLD GAME CASE
    5 - CHARACTER ROW POSITION
    6 - CHARACTER COL POSITION
    7 - CHARACTER START  ROW POSITION
    8 - CHARACTER START COL POSITION
    9 - CHARACTER END  ROW POSITION
    10 - CHARACTER END COL POSITION


    */

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        //bindProperties(viewModel);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //DISPLAY MAZE BY PARAMS THAT SENT FROM NEW GAME / OLD GAME
        generateMaze();

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
            displayMaze(viewModel.getMazeAsArray());

        }
    }

    public void displayMaze(char[][] maze) {
        mazeDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getRowCurrentPosition();
        int characterPositionColumn = viewModel.getColCurrentPosition();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
    }

    public void KeyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }

    public void generateMaze() {
        //get maze params from scene
        int row = 0;
        int col = 0;

        switch (mazeParams[0]){
            case "easy":
                row = 50;
                col = 50;
                break;
            case "medium":
                row = 100;
                col = 200;
                break;
            case "hard":
                row = 200;
                col = 400;
                break;
            case "custom":
                row = Integer.parseInt(mazeParams[1]);
                col = Integer.parseInt(mazeParams[2]);
        }
        viewModel.generateMaze(row, col);

        //displayMaze(maze,startArr[0],startArr[1],endArr[0],endArr[1]);
    }

/*
    public void displayMaze(char[][] maze,int startR, int startC, int endR, int endC) {
        mazeDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
    }

*/

    public void solveMaze(ActionEvent actionEvent) {

        showAlert("Solving maze..");
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }





}
