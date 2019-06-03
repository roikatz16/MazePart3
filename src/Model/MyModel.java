package Model;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.scene.input.KeyCode;


public class MyModel extends Observable implements IModel {

    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private char[] mazeAsArray;

    public void Model() {
        //Raise the servers
        mazeGeneratingServer =new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 5000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }


    @Override
    public Maze generateMaze(int row, int col) {
        //generate maze, change char position
        //todo use servers
        MyMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(row, col);
        return maze;
    }


    public void moveCharacter(KeyCode movement, char[][] array) {
        switch (movement) {
            case NUMPAD8://UP
                if(checkPassability(characterPositionRow+1,characterPositionColumn, array)){
                    characterPositionRow++;
                }
                break;


            case NUMPAD2://DOWN
                if(checkPassability(characterPositionRow-1,characterPositionColumn, array)){
                    characterPositionRow--;
                }
                break;

            case NUMPAD4://LEFT
                if(checkPassability(characterPositionRow,characterPositionColumn-1, array)){
                    characterPositionColumn--;
                }
                break;

            case NUMPAD6://RIGHT
                if(checkPassability(characterPositionRow,characterPositionColumn+1, array)){
                    characterPositionColumn++;
                }
                break;

            case NUMPAD7://UP-LEFT
                if(checkPassability(characterPositionRow+1,characterPositionColumn-1, array)){
                    characterPositionRow++;
                    characterPositionColumn--;
                }
                break;

            case NUMPAD9://UP-RIGHT
                if(checkPassability(characterPositionRow+1,characterPositionColumn+1, array)){
                    characterPositionRow++;
                    characterPositionColumn++;
                }
                break;

            case NUMPAD3://DOWN-RIGHT
                if(checkPassability(characterPositionRow-1,characterPositionColumn+1, array)){
                    characterPositionRow--;
                    characterPositionColumn++;
                }
                break;

            case NUMPAD1://DOWN-LEFT
                if(checkPassability(characterPositionRow-1,characterPositionColumn-1, array)){
                    characterPositionRow--;
                    characterPositionColumn--;
                }
                break;
        }
        setChanged();
        notifyObservers();
    }

    private boolean checkPassability(int row, int col, char[][] array) {
        if(row<0 ||
                col<0 ||
                row>= array.length||
                col>= array[0].length||
                array[row][col]=='1')
        {
            return false;
        }
        return true;
    }


    @Override
    public int[][] getMaze() {
        return new int[0][];
    }

    @Override
    public int getCharacterPositionRow() {
        return 0;
    }

    @Override
    public int getCharacterPositionColumn() {
        return 0;
    }

    @Override
    public int[][] solveMaze() {
        return new int[0][];
    }

}
