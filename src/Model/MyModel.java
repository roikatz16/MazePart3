package Model;
import java.util.Observable;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import javafx.scene.input.KeyCode;

public class MyModel extends Observable implements IModel {

    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;

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
    public void generateMaze(int width, int height) {
        //generate maze, change char position

    }

    @Override
    public void moveCharacter(KeyCode movement) {
        switch (movement) {
            case UP:
                characterPositionRow--;
                break;
            case DOWN:
                characterPositionRow++;
                break;
            case RIGHT:
                characterPositionColumn++;
                break;
            case LEFT:
                characterPositionColumn--;
                break;
        }
        setChanged();
        notifyObservers();
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
