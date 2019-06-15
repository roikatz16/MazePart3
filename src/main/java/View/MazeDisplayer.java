package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import sample.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private char[][] maze;
    private ArrayList<int[]> solutionAsIntegersList;
    private String[] gameParams;
    private int characterPositionRow;
    private int characterPositionColumn;
    private int goalPositionRow;
    private int goalPositionColumn;
    private boolean showSolution;

    /* Region Properties */
    private StringProperty ImageFileNameWall;
    private StringProperty ImageFileNameRedWall;
    private StringProperty ImageFileNameCharacter1;
    private StringProperty ImageFileNameCharacter2;
    private StringProperty ImageFileNameCharacter3;
    private StringProperty ImageFileNameCharacter4;
    private StringProperty ImageFileNameStartAndGoal;
    private StringProperty ImageFileNameSolution;
    private MyViewModel viewModel;


    /**
     * constructor
     */
    public MazeDisplayer(){
        showSolution = true;
        ImageFileNameWall = new SimpleStringProperty();
        ImageFileNameRedWall = new SimpleStringProperty();
        ImageFileNameCharacter1 = new SimpleStringProperty();
        ImageFileNameCharacter2 = new SimpleStringProperty();
        ImageFileNameCharacter3 = new SimpleStringProperty();
        ImageFileNameCharacter4 = new SimpleStringProperty();
        ImageFileNameStartAndGoal = new SimpleStringProperty();
        ImageFileNameSolution = new SimpleStringProperty();
        viewModel = Main.getViewModel();
        gameParams = viewModel.getParams();
    }

    /*
    METHODS:
    1. Update methods (set maze, set solution, set character)
    2. Redraw ( + choose character)
    3. SetReziable methods
    4. Getters & Setters
     */

    /**
     * Update the maze (including goal position) and redraw
     * @param maze
     * @param goalRow
     * @param goalColumn
     */
    public void setMaze(char[][] maze, int goalRow, int goalColumn) {
        this.maze = maze;
        goalPositionRow = goalRow;
        goalPositionColumn = goalColumn;
        redraw();
    }

    /**
     * Update the solution and redraw
     * @param solutionAsIntegersList
     */
    public void setSolutionAsIntegersList(ArrayList<int[]> solutionAsIntegersList) {
        this.solutionAsIntegersList = solutionAsIntegersList;
        redraw();
    }

    /**
     * Draw With Solution
     */
    public void redrawWithSolution() {
        showSolution = true;
        redraw();
    }

    /**
     * Draw Without Solution
     */
    public void redrawWithoutSolution(){
        showSolution = false;
        redraw();
    }

    /**
     * Update the character and redraw
     * @param row
     * @param column
     */
    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    /**
     * Draw:
     * 1. the maze  2. the solution (if needed)  3. start and goal  4. the character
     */
    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

            try {

                // Raising Images and Graphic Contexts
                Image characterImage = chooseCharacter();
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image redWallImage = new Image(new FileInputStream(ImageFileNameRedWall.get()));
                Image startAndGoalImage = new Image(new FileInputStream(ImageFileNameStartAndGoal.get()));
                Image solutionImage = new Image (new FileInputStream(ImageFileNameSolution.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                // Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[0].length; j++) {
                        if (maze[i][j] == '1') {
                            if(i==0 || j==0 || i==maze.length-1 || j==maze[0].length-1){
                                gc.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                                gc.drawImage(redWallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                            }
                            else {
                                gc.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                                gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                            }
                        }
                    }
                }

               // Draw solution (if needed)
               if(solutionAsIntegersList!=null && showSolution){
                   for (int i = 0; i < solutionAsIntegersList.size(); i++) {
                       gc.fillRect(solutionAsIntegersList.get(i)[1] * cellWidth, solutionAsIntegersList.get(i)[0] * cellHeight, cellWidth, cellHeight);
                       gc.drawImage(solutionImage, solutionAsIntegersList.get(i)[1] * cellWidth, solutionAsIntegersList.get(i)[0] * cellHeight, cellWidth, cellHeight);
                   }
               }

                // Draw Start and Goal
                gc.drawImage(startAndGoalImage, goalPositionColumn * cellWidth, goalPositionRow  * cellHeight, cellWidth, cellHeight);
                // Draw character
                gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return character according to the player's choice
     * @throws FileNotFoundException
     */
    public Image chooseCharacter() throws FileNotFoundException {
        switch (gameParams[3]){
            case "Netta":
                return new Image(new FileInputStream(ImageFileNameCharacter1.get()));
            case "Dana":
                return  new Image(new FileInputStream(ImageFileNameCharacter2.get()));
            case "Gali":
                return  new Image(new FileInputStream(ImageFileNameCharacter3.get()));
            case "Izhar":
                return  new Image(new FileInputStream(ImageFileNameCharacter4.get()));
        }
        return new Image(new FileInputStream(ImageFileNameCharacter1.get()));
    }

    /* setReziable methods */
/*

    public double minHeight(double width)
    {
        return 0;
    }

    @Override
    public double maxHeight(double width)
    {
        return 600;
    }


    public double minWidth(double height)
    {
        return 0;
    }

    @Override
    public double maxWidth(double height)
    {
        return 600;
    }


    @Override
    public double prefHeight(double height)
    {

        return getHeight() ;
    }
    @Override
    public double prefWidth(double width){
        return getWidth();
    }
*/
    @Override
    public boolean isResizable()
    {
        return true;
    }

    @Override
    public void resize(double width, double height){
        //super.setWidth(width);
        //super.setHeight(height);
        redraw();
    }

    /* Getters & Setters */

    public String[] getGameParams() {
        return gameParams;
    }

    public void setCharacter(String Character) {
        if (this.gameParams ==null){
            this.gameParams = new String[11];
        }
        gameParams[3] = Character;
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }



    /* region Properties Getters & Setters */

    /* ! NOTICE: even though the set-methods marked as not used,
        they are needed for the fxml class
    */

    public String getImageFileNameWall() { return ImageFileNameWall.get(); }
    public void setImageFileNameWall(String imageFileNameWall) { this.ImageFileNameWall.set(imageFileNameWall); }

    public String getImageFileNameRedWall() {
        return ImageFileNameRedWall.get();
    }
    public void setImageFileNameRedWall(String imageFileNameRedWall) {
        this.ImageFileNameRedWall.set(imageFileNameRedWall);
    }

    public StringProperty imageFileNameRedWallProperty() {
        return ImageFileNameRedWall;
    }

    public String getImageFileNameCharacter1() { return ImageFileNameCharacter1.get();}
    public StringProperty imageFileNameCharacter1Property() { return ImageFileNameCharacter1;}
    public void setImageFileNameCharacter1(String imageFileNameCharacter1) {
        this.ImageFileNameCharacter1.set(imageFileNameCharacter1);
    }

    public String getImageFileNameCharacter2() { return ImageFileNameCharacter2.get(); }
    public StringProperty imageFileNameCharacter2Property() { return ImageFileNameCharacter2; }
    public void setImageFileNameCharacter2(String imageFileNameCharacter2) {
        this.ImageFileNameCharacter2.set(imageFileNameCharacter2);
    }

    public String getImageFileNameCharacter3() { return ImageFileNameCharacter3.get(); }
    public StringProperty imageFileNameCharacter3Property() { return ImageFileNameCharacter3; }
    public void setImageFileNameCharacter3(String imageFileNameCharacter3) {
        this.ImageFileNameCharacter3.set(imageFileNameCharacter3);
    }

    public String getImageFileNameCharacter4() { return ImageFileNameCharacter4.get(); }
    public StringProperty imageFileNameCharacter4Property() { return ImageFileNameCharacter4; }
    public void setImageFileNameCharacter4(String imageFileNameCharacter4) {
        this.ImageFileNameCharacter4.set(imageFileNameCharacter4);
    }

    public String getImageFileNameStartAndGoal() { return ImageFileNameStartAndGoal.get(); }
    public void setImageFileNameStartAndGoal(String imageFileNameStartAndGoal) {
        this.ImageFileNameStartAndGoal.set(imageFileNameStartAndGoal);
    }

    public String getImageFileNameSolution() { return ImageFileNameSolution.get(); }
    public StringProperty imageFileNameSolutionProperty() {  return ImageFileNameSolution; }
    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.ImageFileNameSolution.set(imageFileNameSolution);
    }
}

