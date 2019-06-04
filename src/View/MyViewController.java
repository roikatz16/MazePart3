package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import java.util.Observable;


public class MyViewController extends Controller implements IView {

    @FXML
    public MazeDisplayer mazeDisplayer;

    public void KeyPressed(KeyEvent keyEvent) {
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
}
