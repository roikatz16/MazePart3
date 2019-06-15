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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
    METHODS:
    # generate maze
    # solve maze
    # move character (+ 1. check: passability, winning 2. play audio)
    # save game
    # load game
    # getters & setters

 */

public class MyModel extends Observable implements IModel {

    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    private int currentPositionRow;
    private int currentPositionColumn;
    private Maze maze;
    private ArrayList<AState> solutionAsList;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private String characterName;
    private boolean won;
    private  MediaPlayer mediaPlayer;
    private boolean isWinnigSongPlayed;
    private static final Logger LOG = LogManager.getLogger(); //Log4j2


    /**
     * Constructor
     */
    public MyModel() {
        //Raise the servers
        mazeGeneratingServer =new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 5000, new ServerStrategySolveSearchProblem());

        won=false;// Relevant for wonTheGame method
         isWinnigSongPlayed=false;
    }

    /**
     * Start the servers (1. generate 2. solve)
     */
    public void startServers() {
        mazeGeneratingServer.start();
        LOG.info("start generate maze server port 5400 ");
        solveSearchProblemServer.start();
        LOG.info("start search problem server port 5400 ");
    }

    @Override
    /**
     * Stop the servers
     */
    public void stopServers() {
        try {
            LOG.info("Stopping servers..");
            mazeGeneratingServer.stop();
            solveSearchProblemServer.stop();
            threadPool.shutdown();
            threadPool.awaitTermination(0, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("InterruptedException:", e);
            e.printStackTrace();
        }
    }

    /**
     * Generate maze and notify the observers
     * @param row
     * @param col
     * @param character
     */
    @Override
    public void generateMaze(int row, int col,String character) {
        characterName= character;
        threadPool.execute(() -> {
            generateMazeWithServers(row,col);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOG.error("InterruptedException:", e);
                e.printStackTrace();
            }
            LOG.info("New maze created, maze sizes: "+ row*col);
            currentPositionColumn = maze.getStartPosition().getColumnIndex();
            currentPositionRow = maze.getStartPosition().getRowIndex();
            setChanged();
            notifyObservers("generate");
        });
    }

    /**
     * Generate maze by the server
     * @param row
     * @param col
     */
    private void generateMazeWithServers(int row,int col){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        LOG.info("Server is waiting for clients...");
                        LOG.info("client has been found!");
                        LOG.info("generating maze...");
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = ((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[row*col+18];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);

                    } catch (Exception var10) {
                        LOG.error("InterruptedException:", var10);
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
    /**
     * Solve the maze and notify the observers
     * Returns the solution (int[][]: indexes)
     */
    public int[][] solveMaze() {

        threadPool.execute(() -> {
            LOG.info("Try solve maze...");
            solveMazeWithServers();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOG.error("InterruptedException:", e);
                e.printStackTrace();
            }
            LOG.info("maze has solve save successfully!");
            setChanged();
            notifyObservers("solved");
        });
        return new int[0][];
    }

    /**
     * Solve maze by the server
     */
    public void solveMazeWithServers() {

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {

                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();

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
                        LOG.error("Exception:", e);
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    /**
     * For every movement of the character we:
     * 1. notify the observers (for redrawing)
     * 2. play the matching sound
     * 3. check if we have won
     * @param movement
     * @param array
     */
    public void moveCharacter(KeyCode movement, char[][] array) {
        // mute the winning song
        if(mediaPlayer != null && isWinnigSongPlayed){
            mediaPlayer.stop();
            isWinnigSongPlayed = false;
        }

        switch (movement) {
            case T://UP
                if(checkPassability(currentPositionRow -1, currentPositionColumn, array)){
                    currentPositionRow--;
                    if (characterName.equals("Netta")){
                        playAudio("resources/music/Netta -RII.mp3");

                    }else if (characterName.equals("Gali")){
                        playAudio("resources/music/Gali - ha.mp3");

                    }else if (characterName.equals("Dana")){
                        playAudio("resources/music/Dana - 1.mp3");

                    }else if (characterName.equals("Izhar")){
                        playAudio("resources/music/Izhar - 1.mp3");
                    }
                }
                break;


            case B://DOWN
                if(checkPassability(currentPositionRow +1, currentPositionColumn, array)){
                    currentPositionRow++;
                    if (characterName.equals("Netta")){
                        playAudio("resources/music/Netta - la.mp3");

                    }else if (characterName.equals("Gali")){
                        playAudio("resources/music/Gali - le.mp3");

                    }else if (characterName.equals("Dana")){
                        playAudio("resources/music/Dana - 2.mp3");

                    }else if (characterName.equals("Izhar")){
                        playAudio("resources/music/Izhar - 2.mp3");
                    }
                }
                break;

            case F://LEFT
                if(checkPassability(currentPositionRow, currentPositionColumn -1, array)){
                    currentPositionColumn--;
                    if (characterName.equals("Netta")){
                        playAudio("resources/music/Netta - ha.mp3");

                    }else if (characterName.equals("Gali")){
                        playAudio("resources/music/Gali - lu.mp3");

                    }else if (characterName.equals("Dana")){
                        playAudio("resources/music/Dana - 3.mp3");

                    }else if (characterName.equals("Izhar")){
                        playAudio("resources/music/Izhar - 3.mp3");
                    }
                }
                break;

            case H://RIGHT
                if(checkPassability(currentPositionRow, currentPositionColumn +1, array)){
                    currentPositionColumn++;
                    if (characterName.equals("Netta")){
                        playAudio("resources/music/Netta - foo.mp3");

                    }else if (characterName.equals("Gali")){
                        playAudio("resources/music/Gali - ya.mp3");

                    }else if (characterName.equals("Dana")){
                        playAudio("resources/music/Dana - 4.mp3");

                    }else if (characterName.equals("Izhar")){
                        playAudio("resources/music/Izhar - 4.mp3");
                    }
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


        if (wonTheGame()){
            isWinnigSongPlayed = true;
            if (characterName.equals("Netta")){
                playAudio("resources/music/Netta - won.mp3");

            }else if (characterName.equals("Gali")){
                playAudio("resources/music/Gali - Won.mp3");

            }else if (characterName.equals("Dana")){
                playAudio("resources/music/Dana - Won.mp3");

            }else if (characterName.equals("Izhar")){
                playAudio("resources/music/Izhar - Won.mp3");
            }

        }
        setChanged();
        notifyObservers("move");
    }

    /**
     * Play audion
     * @param audio
     */
    protected void playAudio(String audio) {
        String musicFile = audio;     // For example
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    /**
     * Check if the character has reached the end of the maze
     * @return boolean answer
     */
    private boolean wonTheGame() {
        if(currentPositionColumn==maze.getGoalPosition().getColumnIndex() && currentPositionRow == maze.getGoalPosition().getRowIndex()){
            won = true;
        } else {
            won=false;
        }
        return won;
    }

    /**
     * For the observers
     * Check if the character has reached the end of the maze
     * @return boolean answer
     */
    public boolean isWon() {
        return won;
    }

    /**
     * Check whether the character can move or got to a wall
     * @param row
     * @param col
     * @param array
     * @return boolean answer
     */
    private boolean checkPassability(int row, int col, char[][] array) {
        if(row<0 ||
                col<0 ||
                row >= array.length||
                col >= array[0].length||
                array[row][col]=='1')
        {
            if (characterName.equals("Netta")){
                playAudio("resources/music/Netta - ouch.mp3");
            }
            return false;
        }
        return true;
    }

    /**
     * Save the game
     * include the character's position in the maze
     * @param CharacterPositionRow
     * @param characterPositionCol
     * @param characterName
     * @param fileName
     */
    public void saveGame(int CharacterPositionRow, int characterPositionCol, String characterName, String fileName){
        TimeZone tz = TimeZone.getTimeZone("UTC+3");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd&HH-mm"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        String fileMetaData = fileName+ " "+ nowAsISO + "METADATA";
        LOG.info("Try save game...");

        try (BufferedWriter outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileMetaData),"utf-8"),1024)) {

            outWriter.write(characterName);
            outWriter.newLine();
            outWriter.write(Integer.toString(getCurrentPositionRow()));
            outWriter.newLine();
            outWriter.write(Integer.toString(getCurrentPositionColumn()));
            outWriter.newLine();
            outWriter.write(Integer.toString(maze.toByteArray().length));
            outWriter.newLine();

            LOG.info("game has been save successfully!");
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


    /**
     * Load an old game
     * @param gameTitle
     * @throws IOException
     */
    @Override
    public void loadGame(String gameTitle) throws IOException {
        //extract string data from metaDATA file
        int length = extractMetaDataFile(gameTitle);
        LOG.info("Try Load game...");
        while (gameTitle.charAt(gameTitle.length()-1)>58){
            gameTitle = gameTitle.substring(0,gameTitle.length()-2);
        }
        //extract byte file to maze using decompressor
        extractByteFile(gameTitle,length);
        LOG.info("game has been load successfully!");
        setChanged();
        notifyObservers("loadMaze");

    }

    /**
     * Extract the meta data of loaded game
     * @param gameTitle
     * @return
     * @throws IOException
     */
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

    /**
     * Extract byte file to maze using decompressor
     * @param gameTitle
     * @param length
     */
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


   /* Getters and Setters */

    @Override
    public Maze getMaze() {
        return maze;
    }

    public ArrayList<AState> getSolutionAsList() {
        return solutionAsList;
    }

    //set solutionAsList to null
    public void deleteSolution() {
        this.solutionAsList = null;
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
