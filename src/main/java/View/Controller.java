package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import sample.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observer;

public abstract class Controller implements Observer {
    MyViewModel viewModel;

    // No constructor because it is abstract class

    /**
     * set the viewModel
     * @param viewModel
     */
    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     *  close the program with confirming
     *  close the servers
     */
    protected void closeProgram() {
        viewModel = Main.getViewModel();
        Stage s = Main.getStage();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            s.close();
            viewModel.close();
        }
    }
    /*
    SCENE-CHANGING METHODS:
    1. goToLanding
    2. goToNewGame
    3. goToLoadGame
    4. goToMainScene
     */

    /**
     * Go to landing scene
     * @throws IOException
     */
    protected void goToLanding() throws IOException {
        Stage s = Main.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("../View/Landing.fxml"));
        Scene scene = new Scene(root, 758, 454);
        scene.getStylesheets().add(getClass().getResource("../View/Landing.css").toExternalForm());
        s.setScene(scene);
        Main.setStage(s);
        s.show();
    }

    /**
     * Go to new-game scene
     * @throws IOException
     */
    protected void goToNewGame() throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader();
        Stage s = Main.getStage();
        Parent root = fxmlLoader.load(getClass().getResource("../View/NewGame.fxml").openStream());
        Scene scene = new Scene(root,745,500);
        scene.getStylesheets().add(getClass().getResource("../View/NewGame.css").toExternalForm());
        s.setScene(scene);
        NewGameController ng = fxmlLoader.getController();
        viewModel=Main.getViewModel();
        ng.setViewModel(viewModel);
        viewModel.addObserver(ng);
        Main.setStage(s);
        s.show();
    }

    /**
     * Go to loading scene
     * @throws IOException
     */
    protected void goToLoadGame() throws IOException {
        viewModel = Main.getViewModel();
        viewModel.deleteSolution();
        FXMLLoader fxmlLoader1 = new FXMLLoader();
        Stage d = Main.getStage();
        Parent root = fxmlLoader1.load(getClass().getResource("../View/Load.fxml").openStream());
        Scene scene = new Scene(root,758,500);
        scene.getStylesheets().add(getClass().getResource("../View/Load.css").toExternalForm());
        d.setScene(scene);
        LoadController lc = fxmlLoader1.getController();
        lc.setViewModel(viewModel);

        Main.setStage(d);
        d.show();
    }

    /**
     * Go to Main scene: the game scene
     * @throws IOException
     */
    protected void goToMainScene (FXMLLoader fxmlLoader, Stage stage)throws IOException{
        stage = Main.getStage();

        Parent root = fxmlLoader.load(getClass().getResource("../View/MyView.fxml").openStream());
        Scene scene = new Scene(root,1000,800);
        scene.getStylesheets().add(getClass().getResource("../View/view.css").toExternalForm());
        stage.setScene(scene);
        MyViewController vc = fxmlLoader.getController();
        vc.setResizeEvent(scene);
        vc.setViewModel(viewModel);
        viewModel.addObserver(vc);
    }


    /* iterate over all maze files and get their titles */

    protected ArrayList<String> getTitlesOfFiles(){
        ArrayList<String> mazeTitles = new ArrayList<>();
        File dir = new File(".");
        File[] filesList = dir.listFiles();
        assert filesList != null;
        for (File file : filesList) {
            if (isMeta(file.getName())){
                mazeTitles.add(file.getName());
            }
        }
        while (mazeTitles.size()>6){
            String removedMaze = mazeTitles.remove(0);
            File file = new File(removedMaze);
            file.delete();
            removedMaze = removedMaze.substring(0,removedMaze.length()-8);
            file = new File(removedMaze);
            file.delete();
        }

        return mazeTitles;
    }




    private boolean isMeta(String name){
        if (name.length()>7){

            if(name.charAt(name.length()-1)=='A' && name.charAt(name.length()-2)=='T' && name.charAt(name.length()-3)=='A' ){
                return true;
            }
        }
        return false;
    }

}
