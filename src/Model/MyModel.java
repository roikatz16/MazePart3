package Model;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import Client.Client;
import Client.IClientStrategy;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;


public class MyModel extends Observable implements IModel {

    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    private int currentPositionRow;
    private int currentPositionColumn;
    private Maze maze;
    private ArrayList<AState> solutionAsList;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private String characterName;



    public MyModel() {
        //Raise the servers
        mazeGeneratingServer =new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 5000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
    }

    @Override
    public void stopServers() {
        try {
            mazeGeneratingServer.stop();
            solveSearchProblemServer.stop();
            threadPool.shutdown();
            threadPool.awaitTermination(0, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }


    @Override
    public void generateMaze(int row, int col) {

        //Generate maze
        threadPool.execute(() -> {
            generateMazeWithServers(row,col);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentPositionColumn = maze.getStartPosition().getColumnIndex();
            currentPositionRow = maze.getStartPosition().getRowIndex();
            setChanged();
            notifyObservers("generate");
        });
    }

    private void generateMazeWithServers(int row,int col){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[20018];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                        if(maze != null){
                            maze.print();
                        }

                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }
    @Override
    public int[][] solveMaze() {

        //Generate maze
        threadPool.execute(() -> {
            solveMazeWithServers();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers("solved");
        });
        return new int[0][];
    }


    public void solveMazeWithServers() {

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        //System.out.println(System.getProperty("java.io.tmpdir"));
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        //MyMazeGenerator mg = new MyMazeGenerator();
                        //Maze maze = mg.generate(4, 4);
                        //maze.print();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                        //Print Maze Solution retrieved from the server
                        System.out.println(String.format("Solution steps: %s", mazeSolution));
                        solutionAsList = mazeSolution.getSolutionPath();//todo ?
                        for (int i = 0; i < solutionAsList.size(); i++) {
                            System.out.println(String.format("%s. %s", i, solutionAsList.get(i).toString()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void moveCharacter(KeyCode movement, char[][] array) {
        switch (movement) {
            case T://UP
                if(checkPassability(currentPositionRow -1, currentPositionColumn, array)){
                    currentPositionRow--;
                }
                break;


            case B://DOWN
                if(checkPassability(currentPositionRow +1, currentPositionColumn, array)){
                    currentPositionRow++;
                }
                break;

            case F://LEFT
                if(checkPassability(currentPositionRow, currentPositionColumn -1, array)){
                    currentPositionColumn--;
                }
                break;

            case H://RIGHT
                if(checkPassability(currentPositionRow, currentPositionColumn +1, array)){
                    currentPositionColumn++;
                }
                break;

            case R://UP-LEFT
                if(checkPassability(currentPositionRow -1, currentPositionColumn -1, array)){
                    currentPositionRow--;
                    currentPositionColumn--;
                }
                break;

            case Y://UP-RIGHT
                if(checkPassability(currentPositionRow -1, currentPositionColumn +1, array)){
                    currentPositionRow--;
                    currentPositionColumn++;
                }
                break;

            case N://DOWN-RIGHT
                if(checkPassability(currentPositionRow +1, currentPositionColumn +1, array)){
                    currentPositionRow++;
                    currentPositionColumn++;
                }
                break;

            case V://DOWN-LEFT
                if(checkPassability(currentPositionRow +1, currentPositionColumn -1, array)){
                    currentPositionRow++;
                    currentPositionColumn--;
                }
                break;
        }
        setChanged();
        notifyObservers("move");
    }

    private boolean checkPassability(int row, int col, char[][] array) {
        if(row<0 ||
                col<0 ||
                row >= array.length||
                col >= array[0].length||
                array[row][col]=='1')
        {
            return false;
        }
        return true;
    }

    public void saveGame(int CharacterPositionRow, int characterPositionCol, String characterName, String fileName){
        TimeZone tz = TimeZone.getTimeZone("UTC+3");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd&HH-mm"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        String fileMetaData = fileName+ " "+ nowAsISO + "METADATA";


        try (BufferedWriter outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileMetaData),"utf-8"),1024)) {

            outWriter.write(characterName);
            outWriter.newLine();
            outWriter.write(Integer.toString(getCurrentPositionRow()));
            outWriter.newLine();
            outWriter.write(Integer.toString(getCurrentPositionColumn()));
            outWriter.newLine();
            outWriter.write(Integer.toString(maze.toByteArray().length));
            outWriter.newLine();


        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(fileName+ " " + nowAsISO));
            out.write(maze.toByteArray());
            out.flush();
            out.close();
        } catch (IOException var8) {
            var8.printStackTrace();
        }
    }




    @Override
    public void loadGame(String gameTitle) throws IOException {
        //extract string data from metaDATA file
        int length = extractMetaDataFile(gameTitle);
        while (gameTitle.charAt(gameTitle.length()-1)>58){
            gameTitle = gameTitle.substring(0,gameTitle.length()-2);
        }
        //extract byte file to maze using decompressor
        extractByteFile(gameTitle,length);

        setChanged();
        notifyObservers("loadMaze");

    }

    private int extractMetaDataFile(String gameTitle) throws IOException {
        FileReader fileReader = new FileReader(gameTitle);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line ;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        characterName = lines.get(0);
        currentPositionRow =  Integer.parseInt(lines.get(1));
        currentPositionColumn = Integer.parseInt(lines.get(2));
        return Integer.parseInt(lines.get(3));

    }

    private void extractByteFile(String gameTitle, int length) {
        byte[] savedMazeBytes = new byte[0];

        try {
            InputStream in = new MyDecompressorInputStream(new FileInputStream(gameTitle));
            savedMazeBytes = new byte[length];
            in.read(savedMazeBytes);
            in.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        }
        maze = new Maze(savedMazeBytes);
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    public ArrayList<AState> getSolutionAsList() {
        return solutionAsList;
    }

    public int getCurrentPositionRow() {
        return currentPositionRow;
    }

    public int getCurrentPositionColumn() {
        return currentPositionColumn;
    }

    public void setCurrentPositionRow(int currentPositionRow) {
        this.currentPositionRow = currentPositionRow;
    }

    public void setCurrentPositionColumn(int currentPositionColumn) {
        this.currentPositionColumn = currentPositionColumn;
    }


    @Override
    public String getCharacterName() {
        return characterName;
    }


}
