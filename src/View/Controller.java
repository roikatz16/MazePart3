package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.util.Observer;

public abstract class Controller implements Observer {
    protected MyViewModel viewModel;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        //bindProperties(viewModel);
    }

    protected void closeProgram() {
        Stage s = Main.getStage();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            s.close();
            viewModel.close();
        }

    }

    protected void newGame(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader();
        Stage s = Main.getStage();
        Parent root = fxmlLoader.load(getClass().getResource("../View/NewGame.fxml").openStream());
        s.setScene(new Scene(root, 600, 400));
        NewGameController ng = fxmlLoader.getController();
        ng.setViewModel(viewModel);
        viewModel.addObserver(ng);
        Main.setStage(s);
        s.show();
    }

}
