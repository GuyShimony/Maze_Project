package View;


import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private Position start_point;
    private Position goal_point;
    private int row_player,col_player;

    private double canvasHeight,canvasWidth,cellHeight,cellWidth;

    private Image iWall, iBackground,iPlayer,iFinsish,iHint;
    private Media media = new Media(Paths.get("./resources/Music/BackRoundMusic.mp3").toUri().toString());
    private MediaPlayer musicplayer = new MediaPlayer(media);

    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameGoal = new SimpleStringProperty();
    StringProperty imageFileNameHint = new SimpleStringProperty();
    StringProperty imageBackground = new SimpleStringProperty();

    GraphicsContext graphicsContext;

    public MazeDisplayer() {
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());;
    }

    public String getImageFileNameHint() {
        return imageFileNameHint.get();
    }

    public String getImageBackground() {
        return imageBackground.get();
    }

    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public void setImageFileNameHint(String imageFileNameHint) {
        this.imageFileNameHint.set(imageFileNameHint);
    }

    public void setImageBackground(String imageBackround) {
        this.imageBackground.set(imageBackround);
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }

    public void set_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;
        draw();
    }

    public void init_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;
    }

    public void drawMaze(Maze maze) {

        this.maze = maze;
        start_point = maze.getStartPosition();
        goal_point = maze.getGoalPosition();
        init_player_position(start_point.getRowIndex(),start_point.getColumnIndex());
        try {
            iWall = new Image(new FileInputStream(getImageFileNameWall()));
            iFinsish = new Image(new FileInputStream(getImageFileNameGoal()));
            iPlayer = new Image(new FileInputStream(getImageFileNamePlayer()));
            iHint = new Image(new FileInputStream(getImageFileNameHint()));
            iBackground = new Image(new FileInputStream(getImageBackground()));

        } catch (FileNotFoundException e) {
            System.out.println("There is no Image file...." + e.getMessage());
        }
        playBackgroundMusic();
        draw();
    }

    public void draw()
    {
        if( maze!=null)
        {
            canvasHeight = getHeight();
            canvasWidth = getWidth();

            int row = maze.getRows();
            int col = maze.getColumns();

            cellHeight = canvasHeight/row;
            cellWidth = canvasWidth/col;
            graphicsContext = getGraphicsContext2D();

            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
            graphicsContext.setFill(Color.RED);
            //Draw Maze
            for(int i=0;i<row;i++)
                for(int j=0;j<col;j++)
                    if(!maze.checkCell(i,j)) // Wall
                        drawImage(i,j,getImageFileNameWall(),iWall);
            drawStartAndGoal();
        }
    }

    public void showSolution(ArrayList<String> solution) {
        if (graphicsContext == null)
            return;

        int current_row = start_point.getRowIndex();
        int current_col = start_point.getColumnIndex();
        String direction = "";
        for (int i = 0; i < solution.size();i++) {
            direction = solution.get(i);
            switch (direction) {
                case "UP":
                    drawImage(current_row,current_col,getImageFileNameHint(),iHint);
                    current_row = current_row-1;
                    break;
                case "UP_RIGHT":
                    drawImage(current_row,current_col,getImageFileNameHint(),iHint);
                    current_row = current_row-1;
                    current_col = current_col +1;
                    break;
                case "RIGHT":
                    drawImage(current_row,current_col,getImageFileNameHint(),iHint);
                    current_col = current_col +1;
                    break;
                case "DOWN_RIGHT":
                    drawImage(current_row,current_col,getImageFileNameHint(),iHint);
                    current_row = current_row+1;
                    current_col = current_col+1;
                    break;
                case "DOWN":
                    drawImage(current_row,current_col,getImageFileNameHint(),iHint);
                    current_row = current_row+1;
                    break;
                case "DOWN_LEFT":
                    drawImage(current_row,current_col,getImageFileNameHint(),iHint);
                    current_row = current_row+1;
                    current_col = current_col-1;
                    break;
                case "LEFT":
                    drawImage(current_row,current_col,getImageFileNameHint(),iHint);
                    current_col = current_col-1;
                    break;
                case "UP_LEFT":
                    drawImage(current_row,current_col,getImageFileNameHint(),iHint);
                    current_row = current_row-1;
                    current_col = current_col-1;
                    break;
            }
        }
    }

    public void hideMazeSolution(){draw();}

    /**
     * This function will be called each time the maze is drawn.
     * It will set the start and goal pictures.
     */
    public void drawStartAndGoal(){
        // Draw the start position with the current player
        drawImage(row_player,col_player,getImageFileNamePlayer(),iPlayer);
        // Draw the goal position
        drawImage(goal_point.getRowIndex(),goal_point.getColumnIndex(),getImageFileNameGoal(),iFinsish);
    }

    /**
     * This function will get the path to an image file and will set the character image in the game
     * to the image pointed by the path.
     * After the update was done the maze will be redraw by the draw() function.
     * @param path - Path to the image file of the character.
     */
    public void setPlayerImage(String path){
        try {
            iPlayer = new Image(new FileInputStream(path));
            drawImage(row_player,col_player,path,iPlayer);
            draw();
        } catch (FileNotFoundException e) {
            System.out.println("There is no Image file...." + e.getMessage());
        }
    }

    /**
     * The function will get a path to an image (the finish line image)
     * and set the finish line picture to be the picture pointed by the path arg.
     * After the update was done the maze will be redraw by the draw() function.
     * @param path - the path to the file.
     */
    public void setFinishImage(String path){
        try {
            iFinsish = new Image(new FileInputStream(path));
            drawImage(row_player,col_player,path,iPlayer);
            draw();

        } catch (FileNotFoundException e) {
            System.out.println("There is no Image file...." + e.getMessage());
        }
    }

    /**
     * The function will draw an image in the maze.
     * The picture will be drawn at the coordinates pointed by x and y.
     * If the Image instance that was given is null a new image will be created - pointed by the path argument.
     * @param x_position - x coordinate of the image in the maze
     * @param y_poisiton - y coordinate of the image in the maze
     * @param filepath - path to the image fill. Will be used only if the image arg is null,
     *                 else this can be null as well.
     * @param image - An instance of an Image already initialized.
     */
    public void drawImage(double x_position,double y_poisiton,String filepath,Image image){
        double x = x_position * cellHeight;
        double y = y_poisiton * cellWidth;

        try {
            if(image == null)
                image = new Image(new FileInputStream(filepath));
            graphicsContext.drawImage(image,y,x,cellWidth,cellHeight);
        } catch (FileNotFoundException e) {
            System.out.println("There is no Image file....");
            graphicsContext.fillRect(y,x,cellWidth,cellHeight);
        }
    }

    public void playBackgroundMusic(){
        musicplayer.setVolume(0.2);
        musicplayer.setAutoPlay(true);
        musicplayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicplayer.play();
    }

    public void muteBackgroundMusic(){
        musicplayer.stop();
    }

    @Override
    public boolean isResizable()
    {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        Screen screen = Screen.getPrimary();
        return screen.getVisualBounds().getWidth()-50;
    }

    @Override
    public double prefHeight(double width) {
        Screen screen = Screen.getPrimary();
        return screen.getVisualBounds().getHeight()-50;
    }
}
