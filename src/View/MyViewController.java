package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import sample.Main;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;


public class MyViewController extends Controller implements IView, Initializable {

    //private boolean isMakingMove = false;
    @FXML
    public MazeDisplayer mazeDisplayer;

    public void KeyPressed(KeyEvent keyEvent) {
     //   isMakingMove = true;
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();

    }

    public void solveMaze(ActionEvent actionEvent) {

        showAlert("Solving maze..");
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
            mazeDisplayer.setMaze(viewModel.getMazeAsArray());
            int characterPositionRow = viewModel.getRowCurrentPosition();
            int characterPositionColumn = viewModel.getColCurrentPosition();
            mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage s = Main.getStage();
        s.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });
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
}
