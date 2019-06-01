package sample;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("work");
        MyModel model = new MyModel();
        //model.startServers();
        MyViewModel viewModel = new MyViewModel(model);

        Parent root = FXMLLoader.load(getClass().getResource("../View/MyView.fxml"));
        primaryStage.setTitle("MY APP");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }



}
