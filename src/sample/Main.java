package sample;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        Main.primaryStage = stage;
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        setStage(primaryStage);
        MyModel model = new MyModel();
        //model.startServers();
        MyViewModel viewModel = new MyViewModel(model);

        Parent root = FXMLLoader.load(getClass().getResource("../View/Landing.fxml"));
        primaryStage.setTitle("MY APP");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }



}
