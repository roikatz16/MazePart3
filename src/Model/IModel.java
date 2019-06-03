package Model;

import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;

public interface IModel {

    Maze generateMaze(int width, int height);
    void moveCharacter(KeyCode movement, char[][] array);
    int[][] getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    int [][] solveMaze();


}
