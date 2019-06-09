package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

public class LoadController extends Controller implements Initializable {

    ArrayList<String> mazeTitles;

    ArrayList<MazeTitles> titleOfFiles;

    @FXML
    private Label game1Title,label1,label12,label11,errorLabel;
    @FXML
    private Label game2Title,label2,label21,label22;
    @FXML
    private Label game3Title,label3,label31,label32;
    @FXML
    private Label game4Title,label4,label41,label42;
    @FXML
    private Label game5Title,label5,label51,label52;
    @FXML
    private Label game6Title,label6,label61,label62;
    @FXML
    private Button loadGame1,loadGame2,loadGame3,loadGame4,loadGame5,loadGame6;

    @FXML
    private Label game1Date;
    @FXML
    private Label game2Date;
    @FXML
    private Label game3Date;
    @FXML
    private Label game4Date;
    @FXML
    private Label game5Date;
    @FXML
    private Label game6Date;
    @FXML
    private Label game1Time;
    @FXML
    private Label game2Time;
    @FXML
    private Label game3Time;
    @FXML
    private Label game4Time;
    @FXML
    private Label game5Time;
    @FXML
    private Label game6Time;




    @Override
    public void update(Observable o, Object arg) {


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Stage s = Main.getStage();


        s.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });

        viewModel = Main.getViewModel();

        mazeTitles = getTitlesOfFiles();
        titleOfFiles = orderTitles();
        setTitles();
    }


    private void setTitles() {
        switch (titleOfFiles.size()){
            case 0:
                setTitles0();
                break;
            case 1:
                setTitles1();
                break;
            case 2:
                setTitles2();
                break;
            case 3:
                setTitles3();
                break;
            case 4:
                setTitles4();
                break;
            case 5:
                setTitles5();
                break;
            case 6:
                setTitles6();
                break;

        }
    }



    private ArrayList<MazeTitles> orderTitles() {
        ArrayList<MazeTitles> titleOfFiles = new ArrayList<>();


        for(int m=0;m<mazeTitles.size();m++){
            String mazeTitle ="";
            String dateOfCreation = "";
            String timeOfCreation = "";
            String word = mazeTitles.get(m);
            int i=0;
            while(word.charAt(i)<47||word.charAt(i)>58 ){
                mazeTitle+= word.charAt(i);
                i++;
            }
            word =word.substring(i);
            i=0;
            while(word.charAt(i)!='&'){
                dateOfCreation+= word.charAt(i);
                i++;
            }
            word =word.substring(i+1);
            i=0;
            while(word.charAt(i)!='M'){
                timeOfCreation+= word.charAt(i);
                i++;
            }

            MazeTitles temp = new MazeTitles(mazeTitle,timeOfCreation,dateOfCreation);
            titleOfFiles.add(temp);
        }
        return titleOfFiles;
    }

    private void setTitles0() {
        errorLabel.setVisible(true);
        game1Title.setText("there is no previous game");
        game1Date.setText("create new game");

    }
    private void setTitles1(){
        game1Date.setVisible(true);
        game1Time.setVisible(true);
        game1Title.setVisible(true);
        label1.setVisible(true);
        label12.setVisible(true);
        label11.setVisible(true);
        loadGame1.setVisible(true);
        game1Title.setText(titleOfFiles.get(0).getMazeTitle());
        game1Time.setText(titleOfFiles.get(0).getMazeTimeOfCreation());
        game1Date.setText(titleOfFiles.get(0).getMazeDateOfCreation());
    }

    private void setTitles2(){
        setTitles1();
        label2.setVisible(true);
        label21.setVisible(true);
        label22.setVisible(true);
        game2Date.setVisible(true);
        game2Time.setVisible(true);
        game2Title.setVisible(true);
        loadGame2.setVisible(true);
        game2Title.setText(titleOfFiles.get(1).getMazeTitle());
        game2Time.setText(titleOfFiles.get(1).getMazeTimeOfCreation());
        game2Date.setText(titleOfFiles.get(1).getMazeDateOfCreation());
    }

    private void setTitles3(){
        setTitles2();
        label3.setVisible(true);
        label31.setVisible(true);
        label32.setVisible(true);
        game3Date.setVisible(true);
        game3Time.setVisible(true);
        game3Title.setVisible(true);
        loadGame3.setVisible(true);
        game3Title.setText(titleOfFiles.get(2).getMazeTitle());
        game3Time.setText(titleOfFiles.get(2).getMazeTimeOfCreation());
        game3Date.setText(titleOfFiles.get(2).getMazeDateOfCreation());
    }

    private void setTitles4(){
        setTitles3();
        label4.setVisible(true);
        label41.setVisible(true);
        label42.setVisible(true);
        game4Date.setVisible(true);
        game4Time.setVisible(true);
        game4Title.setVisible(true);
        loadGame4.setVisible(true);
        game4Title.setText(titleOfFiles.get(3).getMazeTitle());
        game4Time.setText(titleOfFiles.get(3).getMazeTimeOfCreation());
        game4Date.setText(titleOfFiles.get(3).getMazeDateOfCreation());
    }

    private void setTitles5(){
        setTitles4();
        label5.setVisible(true);
        label51.setVisible(true);
        label52.setVisible(true);
        game5Date.setVisible(true);
        game5Time.setVisible(true);
        game5Title.setVisible(true);
        loadGame5.setVisible(true);
        game5Title.setText(titleOfFiles.get(4).getMazeTitle());
        game5Time.setText(titleOfFiles.get(4).getMazeTimeOfCreation());
        game5Date.setText(titleOfFiles.get(4).getMazeDateOfCreation());
    }
    private void setTitles6(){
        setTitles5();
        label6.setVisible(true);
        label61.setVisible(true);
        label62.setVisible(true);
        loadGame6.setVisible(true);
        game6Title.setText(titleOfFiles.get(5).getMazeTitle());
        game6Time.setText(titleOfFiles.get(5).getMazeTimeOfCreation());
        game6Date.setText(titleOfFiles.get(5).getMazeDateOfCreation());
        game6Date.setVisible(true);
        game6Time.setVisible(true);
        game6Title.setVisible(true);
    }


    public void goToNewGame(ActionEvent actionEvent) throws IOException {
        newGame(actionEvent);

    }

    public void backToLandingPage(ActionEvent actionEvent) throws IOException {
        Stage s = Main.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("../View/Landing.fxml"));
        s.setScene(new Scene(root, 600, 400));
        Main.setStage(s);
        s.show();

    }

    public void loadGame1(ActionEvent actionEvent) throws IOException {
        viewModel.loadGame(mazeTitles.get(0));
        LoadingGame();
    }

    public void loadGame2(ActionEvent actionEvent) throws IOException {
        viewModel.loadGame(mazeTitles.get(1));
        LoadingGame();
    }

    public void loadGame3(ActionEvent actionEvent) throws IOException {
        viewModel.loadGame(mazeTitles.get(2));
        LoadingGame();
    }

    public void loadGame4(ActionEvent actionEvent) throws IOException {
        viewModel.loadGame(mazeTitles.get(3));
        LoadingGame();
    }
    public void loadGame5(ActionEvent actionEvent) throws IOException {
        viewModel.loadGame(mazeTitles.get(4));
        LoadingGame();
    }

    public void loadGame6(ActionEvent actionEvent) throws IOException {
        viewModel.loadGame(mazeTitles.get(5));
        LoadingGame();
    }

    public void LoadingGame() throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader();

        Stage s = Main.getStage();
        Parent root = fxmlLoader.load(getClass().getResource("../View/MyView.fxml").openStream());
        s.setScene(new Scene(root, 1000, 800));
        MyViewController vc = fxmlLoader.getController();

        vc.setViewModel(viewModel);
        viewModel.addObserver(vc);

        Main.setStage(s);

        s.show();
    }



}
