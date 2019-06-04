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

public class MyViewController extends Controller implements IView, Initializable {



    @FXML
    private MazeDisplayer mazeDisplayer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //DISPLAY MAZE BY PARAMS THAT SENT FROM NEW GAME / OLD GAME

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
        mazeDisplayer.getId();
        mazeDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getRowCurrentPosition();
        int characterPositionColumn = viewModel.getColCurrentPosition();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
    }

    public void KeyPressed(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
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
