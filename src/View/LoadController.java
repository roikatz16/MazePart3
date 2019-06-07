package View;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class LoadController extends Controller implements Initializable {



    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            System.out.println("awsome");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //viewModel.loadAllPreviousMazes();

    }
}
