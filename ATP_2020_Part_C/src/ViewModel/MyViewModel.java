package ViewModel;

import Model.FinishException;
import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import com.sun.media.sound.InvalidDataException;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.io.*;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    private StringProperty maze_rows, maze_cols;
    //hashmap for the player and finish images. will be set by the user and switched during the game
    private HashMap<String,String>Character_ImagePath;
    private HashMap<String,String>Finish_ImagePath;
    //holds the current characater type + direction
    private String character_id ="";

    @Override
    public void update(Observable o, Object arg) {
        this.setChanged();
        if(arg instanceof int[])
            this.notifyObservers((int[])arg);
        else if(arg instanceof Position[])
            this.notifyObservers((Position[])arg);
        else if(arg instanceof Maze)
            this.notifyObservers((Maze)arg);
    }


    public MyViewModel(IModel model) {
        this.model = model;
        Character_ImagePath = new HashMap<>();
        Finish_ImagePath = new HashMap<>();
        initializeCharactersDict();
        initializeFinishDict();
    }
    private void initializeCharactersDict(){
        Character_ImagePath.put("mario_right","./resources/Images/mario_right.png");
        Character_ImagePath.put("mario_left","./resources/Images/mario_left.png");
        Character_ImagePath.put("luigi_right","./resources/Images/luigi_right.png");
        Character_ImagePath.put("luigi_left","./resources/Images/luigi_left.png");
        Character_ImagePath.put("princess_right","./resources/Images/princess_right.png");
        Character_ImagePath.put("princess_left","./resources/Images/princess_left.png");
        Character_ImagePath.put("bowser_right","./resources/Images/bowser_right.png");
        Character_ImagePath.put("bowser_left","./resources/Images/bowser_left.png");
        Character_ImagePath.put("turtle_right","./resources/Images/turtle_right.png");
        Character_ImagePath.put("turtle_left","./resources/Images/turtle_left.png");
        Character_ImagePath.put("toad_right","./resources/Images/toad_right.png");
        Character_ImagePath.put("toad_left","./resources/Images/toad_left.png");

    }

    private void initializeFinishDict(){
        Finish_ImagePath.put("mario_right","./resources/Images/school_bus.png");
        Finish_ImagePath.put("mario_left","./resources/Images/school_bus.png");
        Finish_ImagePath.put("luigi_right","./resources/Images/pencil.png");
        Finish_ImagePath.put("luigi_left","./resources/Images/pencil.png");
        Finish_ImagePath.put("princess_right","./resources/Images/sharpener.png");
        Finish_ImagePath.put("princess_left","./resources/Images/sharpener.png");
        Finish_ImagePath.put("bowser_right","./resources/Images/calculator.png");
        Finish_ImagePath.put("bowser_left","./resources/Images/calculator.png");
        Finish_ImagePath.put("turtle_right","./resources/Images/pen.png");
        Finish_ImagePath.put("turtle_left","./resources/Images/pen.png");
        Finish_ImagePath.put("toad_right","./resources/Images/notepad.png");
        Finish_ImagePath.put("toad_left","./resources/Images/notepad.png");

    }


    public void updatePlayerPosition(KeyEvent keyEvent) throws FinishException {
        String direction = "";
        String character = "";
        int name_index = character_id.indexOf('_');
        if(name_index<0)
            character = "mario";
        else
            character = character_id.substring(0,name_index);
        switch (keyEvent.getCode()) {
            case DOWN: // Move player DOWN  2
                direction = "DOWN";
                break;
            case UP: // Move player UP  8
                direction = "UP";
                break;
            case RIGHT: // Move player RIGHT 6
                direction = "RIGHT";
                setChanged();
                notifyObservers(Character_ImagePath.get(character+"_right"));
                break;
            case LEFT: // Move player LEFT 4
                direction = "LEFT";
                setChanged();
                notifyObservers(Character_ImagePath.get(character+"_left"));
                break;
            case NUMPAD1: // Move player DOWN and LEFT
                direction = "DOWN_LEFT";
                setChanged();
                notifyObservers(Character_ImagePath.get(character+"_left"));
                break;
            case NUMPAD3: // Move player DOWN and RIGHT
                direction = "DOWN_RIGHT";
                setChanged();
                notifyObservers(Character_ImagePath.get(character+"_right"));
                break;
            case NUMPAD7: // Move player UP and LEFT
                direction = "UP_LEFT";
                setChanged();
                notifyObservers(Character_ImagePath.get(character+"_left"));
                break;
            case NUMPAD9: // Move player UP and RIGHT
                direction = "UP_RIGHT";
                setChanged();
                notifyObservers(Character_ImagePath.get(character+"_right"));
                break;
            default: // Don't move player
                direction = "STAY";
                break;
        }
        model.setPlayerLocation(direction);
    }

    /**
     * This function will update the player image every time the user changes the player type
     * @param mouseEvent
     */
    public void updateCharacterType(MouseEvent mouseEvent) {
        character_id = mouseEvent.getPickResult().getIntersectedNode().getId();
        String char_path = Character_ImagePath.get(character_id);
        String finish_path = Finish_ImagePath.get(character_id);
        setChanged();
        notifyObservers(new String[] {char_path,finish_path});
    }

    /*
    The binding method for the maze rows property
     */
    public void setMaze_rows(StringProperty maze_rows) {
        this.maze_rows = maze_rows;
    }

    /*
    The binding method for the maze columns property
     */
    public void setMaze_cols(StringProperty maze_cols) {
        this.maze_cols = maze_cols;
    }

    /*
    The binding method for the maze difficulty property
     */
    public void setMaze_difficulty(String maze_difficulty) {
        if(maze_difficulty.equals("Easy Maze"))
            maze_difficulty = "SimpleMazeGenerator";
        else
            maze_difficulty = "MyMazeGenerator";

        model.setGameDifficulty(maze_difficulty);
    }

    /**
     * The binding method for the maze.
     * The ViewModel is the observable and when the maze changes it notifies all
     * the observers (Views) of the change.
     * @throws InputMismatchException - if the rows/ cols are invalid an exception will be raised
     */
    public void createGameForView() throws InvalidDataException,InputMismatchException {
        if(maze_cols == null || maze_rows == null)
            throw new InputMismatchException();

        int int_maze_rows = validateInput(maze_rows.getValue());
        int int_maze_cols = validateInput(maze_cols.getValue());
        if(int_maze_rows == 1 & int_maze_cols ==1)
            throw new InvalidDataException("small");
        if(int_maze_rows > 500 ||  int_maze_cols > 500)
            throw new InvalidDataException("large");
        model.createGame(int_maze_rows,int_maze_cols);
    }

    /**
     * function gets the solution path from the model (array of AState) and convers to Array of string
     * send the solution update back to the view so it can handle it.
     * @throws NullPointerException -if the solution is null meaning no solution is available
     */
    public void showSolutionViewModel() throws NullPointerException{
        ArrayList<AState> state_path = model.getSolution().getSolutionPath();
        ArrayList<String> direction_path = new ArrayList<>();
        MazeState current = (MazeState)state_path.get(0);
        for (int i = 1; i <state_path.size() ; i++) {
            MazeState next = (MazeState) state_path.get(i);
            int next_row = next.getPosition().getRowIndex();
            int next_col = next.getPosition().getColumnIndex();

            //move up
            if(current.getPosition().getRowIndex()-1==next_row && current.getPosition().getColumnIndex()==next_col)
                direction_path.add("UP");
                //move up and right
            else if(current.getPosition().getRowIndex()-1==next_row && current.getPosition().getColumnIndex()+1==next_col)
                direction_path.add("UP_RIGHT");
                //move right
            else if(current.getPosition().getRowIndex()==next_row && current.getPosition().getColumnIndex()+1==next_col)
                direction_path.add("RIGHT");
                //move down and right
            else if(current.getPosition().getRowIndex()+1==next_row && current.getPosition().getColumnIndex()+1==next_col)
                direction_path.add("DOWN_RIGHT");
                //move down
            else if(current.getPosition().getRowIndex()+1==next_row && current.getPosition().getColumnIndex()==next_col)
                direction_path.add("DOWN");
                //move down and left
            else if(current.getPosition().getRowIndex()+1==next_row && current.getPosition().getColumnIndex()-1==next_col)
                direction_path.add("DOWN_LEFT");
                //move left
            else if(current.getPosition().getRowIndex()==next_row && current.getPosition().getColumnIndex()-1==next_col)
                direction_path.add("LEFT");
                //move up and left
            else if(current.getPosition().getRowIndex()-1==next_row && current.getPosition().getColumnIndex()-1==next_col)
                direction_path.add("UP_LEFT");
            current = next;        }
        setChanged();
        notifyObservers(direction_path);
    }

    public void saveMaze(File file) throws InvalidDataException,FileSystemNotFoundException,IOException {
        if(file == null)
            throw new FileSystemNotFoundException();

        byte[] compressed_maze = model.getCompressedMaze();

        if(compressed_maze==null)
            throw new InvalidDataException();

        WriteToFile(file.getAbsolutePath(),compressed_maze);

    }

    public void loadMaze(File file) throws IOException, ClassNotFoundException {

        FileInputStream fileIn = new FileInputStream(file.getAbsoluteFile());
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        Object obj = objectIn.readObject();
        objectIn.close();

        if(obj == null)
            throw new IOException();

        model.loadGame((byte[])obj);
    }

    /**
     * writes the compressed maze to the file given
     * @param path - absolute path of the file
     * @param data - the compressed maze
     * @throws IOException
     */
    public synchronized void WriteToFile(String path, Object data) throws IOException {

        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(data);
        fileOut.flush();
        fileOut.close();
    }

    /**
     * validation methods for maze size inputs
     * @param str
     * @return
     */
    private boolean isNumeric(String str){
        return str.chars().allMatch( Character::isDigit );
    }

    private int validateInput(String input) throws InputMismatchException{
        if(!isNumeric(input))
            throw new InputMismatchException();
        int user_int_input = Integer.valueOf(input);
        if(user_int_input<=0)
            throw new InputMismatchException();
        return user_int_input;
    }
}
