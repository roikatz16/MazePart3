package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;

public interface IModel {

    void stopServers();
    //--------------------//
    void generateMaze(int width, int height,String character);
    int [][] solveMaze();
    void deleteSolution();
    void moveCharacter(KeyCode movement, char[][] array);
    void loadGame(String gameDate) throws IOException;
    void saveGame(int characterPositionRow, int characterPositionCol, String characterName, String fileName);
    boolean isWon();
    //--------------------//
    /* getters & setters */
    Maze getMaze();
    ArrayList<AState> getSolutionAsList();
    String getCharacterName();
    int getCurrentPositionRow();
    int getCurrentPositionColumn();
    void setCurrentPositionRow(int row);
    void setCurrentPositionColumn(int col);


    void stopMediaPlayer();
    boolean isWinnigSongPlayed();
    void setWinnigSongPlayed(boolean winnigSongPlayed);
    MediaPlayer getMediaPlayer();
}
