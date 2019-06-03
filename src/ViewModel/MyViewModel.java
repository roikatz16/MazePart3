package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.scene.input.KeyCode;

public class MyViewModel {

    private IModel model;

    public Maze maze;

    public MyViewModel(IModel model) {
        this.model = model;
    }

    public char[][] generateMaze(int row, int col){
        maze= model.generateMaze(row,col);
        return maze.getArray();
    }

    public int[] getStartPosition() {
        int[] ans= new int[2];
        Position p = maze.getStartPosition();
        ans[0]=p.getRowIndex();
        ans[1]=p.getColumnIndex();
        return ans;
    }

    public int[] getEndPosition() {
        int[] ans= new int[2];
        Position p = maze.getGoalPosition();
        ans[0]=p.getRowIndex();
        ans[1]=p.getColumnIndex();
        return ans;
    }
    public void moveCharacter(){
        //model.moveCharacter(KeyCode movement);
    }
}
