package ViewModel;

import Model.IModel;
import algorithms.search.AState;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private String[] params;
    private char[][] mazeAsArray;
    private ArrayList<int[]> solutionAsIntegersList;

    private int rowStartPosition;
    private int colStartPosition;
    private int rowGoalPosition;
    private int colGoalPosition;
    private int rowCurrentPosition;
    private int colCurrentPosition;
    private boolean won;


    public MyViewModel(IModel model) {
        this.model = model;
        won=false;
    }
    public void generateMaze(int row, int col,String character){
      model.generateMaze(row, col,character);
    }
    public void solveMaze() { model.solveMaze();}

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            mazeAsArray = (model.getMaze()).getArray();
            solutionAsIntegersList = convert(model.getSolutionAsList());
            rowGoalPosition = (model.getMaze()).getGoalPosition().getRowIndex() ;
            colGoalPosition = (model.getMaze()).getGoalPosition().getColumnIndex() ;
            rowStartPosition = (model.getMaze()).getStartPosition().getRowIndex() ;
            colStartPosition = (model.getMaze()).getStartPosition().getColumnIndex() ;
            rowCurrentPosition = model.getCurrentPositionRow();
            colCurrentPosition = model.getCurrentPositionColumn();
            won = model.isWon();
        }

        //neto fot the character
       if(o==model && arg.toString().equals("loadMaze")){
            params = new String[11];
            params[3] = model.getCharacterName();
        }

        setChanged();
        notifyObservers();

    }


    private ArrayList<int[]> convert (ArrayList<AState> ASlist){
        if (ASlist == null){
            return null;
        }
        ArrayList<int[]> result = new ArrayList<>();
        String word;
        String[] parts;
        String rowString;
        String colString;

        for (int i = 0; i < ASlist.size(); i++) {
            word = ASlist.get(i).toString().replace("(", "").replace(")", "");
            parts =  word.split(",");
            rowString = parts[0];
            colString = parts[1];
            int[] pare = new int[2];
            pare[0] = Integer.parseInt(rowString);
            pare[1] =  Integer.parseInt(colString);
            result.add(pare);
        }

        return result;
    }

    public void close(){
        model.stopServers();
    }

    public boolean isWon() {
        return won;
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement, mazeAsArray);
    }

    public char[][] getMazeAsArray() {
        return mazeAsArray;
    }
    public ArrayList<int[]> getSolutionAsIntegersList() {
        return solutionAsIntegersList;
    }

    public void deleteSolution() {
        this.solutionAsIntegersList = null;
        model.deleteSolution();

    }

    public int getRowStartPosition() { return rowStartPosition; }
    public int getColStartPosition() { return colStartPosition; }
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

    public void setRowCurrentPosition(int rowCurrentPosition) {
        this.rowCurrentPosition = rowCurrentPosition;
        model.setCurrentPositionRow(rowCurrentPosition);
    }

    public void setColCurrentPosition(int colCurrentPosition) {
        this.colCurrentPosition = colCurrentPosition;
        model.setCurrentPositionColumn(colCurrentPosition);
    }

    public String[] getParams() {
        return params;
    }
    public void setParams(String[] params) {
        this.params = params;
    }

    public void saveGame(int characterPositionRow ,int characterPositionCol, String characterName, String fileName){
        model.saveGame(characterPositionRow,characterPositionCol,characterName,fileName);
    }

    public void loadGame(String gameTitle) throws IOException {
        model.loadGame(gameTitle);
    }
}
