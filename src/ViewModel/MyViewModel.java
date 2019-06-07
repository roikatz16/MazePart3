package ViewModel;

import Model.IModel;
import javafx.scene.input.KeyCode;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    private String[] params;

    private char[][] mazeAsArray;
    private int rowStartPosition;
    private int colStartPosition;
    private int rowGoalPosition;
    private int colGoalPosition;
    private int rowCurrentPosition;
    private int colCurrentPosition;



    public MyViewModel(IModel model) {
        this.model = model;
    }

    public void generateMaze(int row, int col){
      model.generateMaze(row, col);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            if(arg!=null&& arg instanceof Boolean && !(Boolean)arg){

            }
            mazeAsArray = (model.getMaze()).getArray();//possible not update charArray every time?
            rowStartPosition = (model.getMaze()).getStartPosition().getRowIndex() ;
            colStartPosition = (model.getMaze()).getStartPosition().getColumnIndex() ;
            rowGoalPosition = (model.getMaze()).getGoalPosition().getRowIndex() ;
            colGoalPosition = (model.getMaze()).getGoalPosition().getColumnIndex() ;
            rowCurrentPosition = model.getCurrentPositionRow();
            colCurrentPosition = model.getCurrentPositionColumn();

            setChanged();
            notifyObservers();
        }
    }

    public void close(){
        model.stopServers();
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement, mazeAsArray);
    }


    public char[][] getMazeAsArray() {
        return mazeAsArray;
    }

    public int getRowStartPosition() {
        return rowStartPosition;
    }

    public int getColStartPosition() {
        return colStartPosition;
    }

    public int getRowGoalPosition() {
        return rowGoalPosition;
    }

    public int getColGoalPosition() {
        return colGoalPosition;
    }

    public int getRowCurrentPosition() {
        return rowCurrentPosition;
    }

    public int getColCurrentPosition() {
        return colCurrentPosition;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
