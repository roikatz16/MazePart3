package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public interface IModel {

    void generateMaze(int width, int height);
    void moveCharacter(KeyCode movement, char[][] array);
    Maze getMaze();
    ArrayList<AState> getSolutionAsList();
    int getCurrentPositionRow();
    int getCurrentPositionColumn();
    void setCurrentPositionRow(int row);
    void setCurrentPositionColumn(int col);
    int [][] solveMaze();
    void stopServers();


    void saveGame(int characterPositionRow, int characterPositionCol, String characterName, String fileName);


}
