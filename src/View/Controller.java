package View;

import ViewModel.MyViewModel;

import java.util.Observer;

public abstract class Controller implements Observer {
    protected MyViewModel viewModel;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        //bindProperties(viewModel);
    }

}
