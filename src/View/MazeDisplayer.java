package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {

    private char[][] maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private String[] gameParams;

    //region Properties
    private StringProperty ImageFileNameWall;
    private StringProperty ImageFileNameCharacter;
    private StringProperty ImageFileNameCharacter1;
    private StringProperty ImageFileNameCharacter2;
    private StringProperty ImageFileNameCharacter3;
    private StringProperty ImageFileNameCharacter4;
    private MyViewModel viewModel;



    public MazeDisplayer(){
        ImageFileNameWall = new SimpleStringProperty();
        ImageFileNameCharacter = new SimpleStringProperty();

        ImageFileNameCharacter1 = new SimpleStringProperty();
        ImageFileNameCharacter2 = new SimpleStringProperty();
        ImageFileNameCharacter3 = new SimpleStringProperty();
        ImageFileNameCharacter4 = new SimpleStringProperty();
        viewModel = Main.getViewModel();
        gameParams = viewModel.getParams();

    }


    public int getCharacterPositionRow() {

        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {

        return characterPositionColumn;
    }

    public String[] getGameParams() {
        return gameParams;
    }

    public void setMaze(char[][] maze) throws FileNotFoundException {
        this.maze = maze;
        Image characterImage =  chooseCharacter();
        redraw(characterImage);
    }

    public void setCharacterPosition(int row, int column) throws FileNotFoundException {
        characterPositionRow = row;
        characterPositionColumn = column;
        Image characterImage =  chooseCharacter();
        redraw(characterImage);
    }

    private Image chooseCharacter() throws FileNotFoundException {
        Image characterImage;
        System.out.println("merge");
        switch (gameParams[3]){
            case "Netta":
                return characterImage = new Image(new FileInputStream(ImageFileNameCharacter1.get()));
            case "Dana":
                return characterImage = new Image(new FileInputStream(ImageFileNameCharacter2.get()));
            case "Gali":
                return characterImage = new Image(new FileInputStream(ImageFileNameCharacter3.get()));
            case "Izhar":
                return characterImage = new Image(new FileInputStream(ImageFileNameCharacter4.get()));
        }
        return characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
    }


    public void redraw(Image characterImage) {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));


                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < maze[0].length; i++) {
                    for (int j = 0; j < maze.length; j++) {
                        if (maze[j][i] == '1') {
                            //gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            gc.drawImage(wallImage, i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                        }
                    }
                }

                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);

            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }

    }


    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public String getImageFileNameCharacter1() {
        return ImageFileNameCharacter1.get();
    }

    public StringProperty imageFileNameCharacter1Property() {
        return ImageFileNameCharacter1;
    }

    public String getImageFileNameCharacter2() {
        return ImageFileNameCharacter2.get();
    }

    public StringProperty imageFileNameCharacter2Property() {
        return ImageFileNameCharacter2;
    }

    public String getImageFileNameCharacter3() {
        return ImageFileNameCharacter3.get();
    }

    public StringProperty imageFileNameCharacter3Property() {
        return ImageFileNameCharacter3;
    }

    public String getImageFileNameCharacter4() {
        return ImageFileNameCharacter4.get();
    }

    public StringProperty imageFileNameCharacter4Property() {
        return ImageFileNameCharacter4;
    }

    public void setImageFileNameCharacter1(String imageFileNameCharacter1) {
        this.ImageFileNameCharacter1.set(imageFileNameCharacter1);
    }

    public void setImageFileNameCharacter2(String imageFileNameCharacter2) {
        this.ImageFileNameCharacter2.set(imageFileNameCharacter2);
    }

    public void setImageFileNameCharacter3(String imageFileNameCharacter3) {
        this.ImageFileNameCharacter3.set(imageFileNameCharacter3);
    }

    public void setImageFileNameCharacter4(String imageFileNameCharacter4) {
        this.ImageFileNameCharacter4.set(imageFileNameCharacter4);
    }
}

