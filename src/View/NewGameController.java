package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class NewGameController extends Controller implements Initializable{


    String[] gameParams = new String[11];

    public TextField row;
    public TextField col;
    public Text rowLabel;
    public Text colLabel;
    public Button submitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = Main.getViewModel();


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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            s.close();
        }

    }

    public void backToLanding(ActionEvent actionEvent) throws IOException {
        Stage s = Main.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("../View/Landing.fxml"));
        s.setScene(new Scene(root, 600, 400));
        Main.setStage(s);
        s.show();

    }


    public void pressedCustom(ActionEvent actionEvent) {
            gameParams[0]="custom";
            row.setVisible(true);
            col.setVisible(true);
            rowLabel.setVisible(true);
            colLabel.setVisible(true);
            row.setDisable(false);
            col.setDisable(false);
            submitButton.setVisible(true);
    }

    public void keyReleasedProperly(KeyEvent keyEvent) {
        String rowText = row.getText();
        String colText = col.getText();
        boolean isDisabled = rowText.trim().isEmpty()||rowText.isEmpty()||colText.trim().isEmpty()||colText.isEmpty();
        submitButton.setDisable(isDisabled);
    }

    public void getCustomMazeSize(ActionEvent actionEvent) {
        if (isInt(row,row.getText()) && isInt(col,col.getText()) ){
            gameParams[2] = row.getText();
            gameParams[3] = col.getText();
        }


    }


    //Validate age
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



    public void StartGame(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader();
        Stage s = Main.getStage();
        Parent root = fxmlLoader.load(getClass().getResource("../View/MyView.fxml").openStream());
        s.setScene(new Scene(root, 600, 400));
        MyViewController vc = fxmlLoader.getController();
        vc.setViewModel(viewModel);
        viewModel.addObserver(vc);
        Main.setStage(s);
        generateMaze();
        s.show();
    }
    /*
        ///////////////MAZE PARAMS/////////////
        0 - DIFFICULTY
        1 - DIFFICULTY (CUSTOM CASE) ROWS
        2 - DIFFICULTY (CUSTOM CASE) COLUMNS
        2 - CHARACTER
        3 - THEME
        4 - TIME (OLD GAME CASE)
        5 - CHARACTER ROW POSITION
        6 - CHARACTER COL POSITION
        7 - CHARACTER START  ROW POSITION
        8 - CHARACTER START COL POSITION
        9 - CHARACTER END  ROW POSITION
        10 - CHARACTER END COL POSITION


        */
    private void generateMaze() {
        //get maze params from scene
        System.out.println(gameParams[0]);
        int row = 0;
        int col = 0;

        switch (gameParams[0]){
            case "easy":
                System.out.println(gameParams[0]);
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
                row = Integer.parseInt(gameParams[1]);
                col = Integer.parseInt(gameParams[2]);
        }
        viewModel.generateMaze(row, col);
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean bool = false;
    }

    public void pickEasy(ActionEvent actionEvent) {
        gameParams[0]="easy";
    }
    public void pickMedium(ActionEvent actionEvent) {
        gameParams[0]="medium";
    }
    public void PickHard(ActionEvent actionEvent) {
        gameParams[0]="hard";
    }

    public void pickPikacu(ActionEvent actionEvent) {
        gameParams[1]="pikacu";
    }
    public void pickStowe(MouseEvent mouseEvent) {
        gameParams[1]="stowe";
    }
    public void pickNetta(MouseEvent mouseEvent) {
        gameParams[1]="netta";
    }
    public void pickHomer(MouseEvent mouseEvent) {
        gameParams[1]="homer";
    }

    public void pickTreeTheme(ActionEvent actionEvent) {
        gameParams[2]="tree";
    }
}

