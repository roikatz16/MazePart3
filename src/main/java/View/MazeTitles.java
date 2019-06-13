package View;

public class MazeTitles {

    private String MazeTitle;
    private String MazeTimeOfCreation;
    private String MazeDateOfCreation;

    public MazeTitles(String mazeTitle, String mazeTimeOfCreation, String mazeDateOfCreation) {
        MazeTitle = mazeTitle;
        MazeTimeOfCreation = mazeTimeOfCreation;
        MazeDateOfCreation = mazeDateOfCreation;
    }

    public String getMazeDateOfCreation() {
        return MazeDateOfCreation;
    }

    public String getMazeTitle() {
        return MazeTitle;
    }

    public String getMazeTimeOfCreation() {
        return MazeTimeOfCreation;
    }

    public void setMazeTitle(String mazeTitle) {
        MazeTitle = mazeTitle;
    }

    public void setMazeTimeOfCreation(String mazeTimeOfCreation) {
        MazeTimeOfCreation = mazeTimeOfCreation;
    }
}
