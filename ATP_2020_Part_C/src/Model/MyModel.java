package Model;
import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Configurations;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
import View.Main;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {

    private ArrayList<Observer> ModelObservers;

    private Solution solution;
    private int player_current_row =-1, player_current_col=-1;
    private Maze maze;
    private byte[] compressed_maze;


    @Override
    public void notifyAllObservers(Object arg) {
        for (Observer obj: ModelObservers)
        {obj.update(this,arg);
        };
    }

    public void addObserver(Object vm){
        ModelObservers.add((MyViewModel) vm);
    }

    public MyModel() {
        this.ModelObservers = new ArrayList<>();
    }

    public void createGame(int maze_rows, int maze_cols){
        try {
            // Create a new client
            // Open a stream to the Maze generator server and ask for a maze
            // using the appropriate IClientStrategy
            GenerateMazeStrategy generateMazeStrategy = new GenerateMazeStrategy(maze_rows,maze_cols);
            Client client = new Client(InetAddress.getLocalHost(), Main.gen_port-2, generateMazeStrategy);
            client.communicateWithServer();
            // Get the maze from the client
            maze = generateMazeStrategy.getMaze();
            // The model will need an instance of the compressed maze to be used by the view model
            // for saving the current maze in a file
            compressed_maze = generateMazeStrategy.getCompressed_maze();
            // Set the start point and goal point for the game
            setStartPlayerPosition(maze.getStartPosition());
            solveTheGame(maze);
            this.setChanged();
            notifyAllObservers(maze);
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }
    }

    public Solution getSolution() throws NullPointerException{
        if(maze == null)
            throw new NullPointerException();
        // If the maze is a new maze need to solve it
        if(solution==null)
            solveTheGame(maze);

        return solution;
    }

    private void solveTheGame(Object obj){
        Maze maze = (Maze)obj;
        // Create a new client
        // Open a stream to the Maze solving server and ask for a maze solution
        // using the appropriate IClientStrategy
        SolvingMazeStrategy solvingMazeStrategy = new SolvingMazeStrategy(maze);
        try{
            Client client = new Client(InetAddress.getLocalHost(), Main.solve_port-2, solvingMazeStrategy);
            client.communicateWithServer();
            // Get the solution from the client
            solution = solvingMazeStrategy.getMazeSolution();}
        catch(UnknownHostException uke){
            uke.printStackTrace();
        }
    }

    @Override
    public void setPlayerLocation(String direction) throws FinishException{
        if(player_current_row ==-1 || player_current_col==-1 ) { //maze was loaded and locations need to be set
            setStartPlayerPosition(maze.getStartPosition());
        }
        int player_row_toMove =player_current_row,player_col_toMove=player_current_col;
        switch (direction) {
            case "UP": // Move player UP
                player_row_toMove = player_current_row -1;
                break;
            case "DOWN": // Move player DOWN
                player_row_toMove = player_current_row +1;
                break;
            case "RIGHT": // Move player RIGHT
                player_col_toMove = player_current_col +1;
                break;
            case "LEFT": // Move player LEFT
                player_col_toMove = player_current_col -1;
                break;
            case "DOWN_LEFT": // Move player DOWN and LEFT
                player_row_toMove = player_current_row +1;
                player_col_toMove = player_current_col -1;
                break;
            case "DOWN_RIGHT": // Move player DOWN and RIGHT
                player_row_toMove = player_current_row +1;
                player_col_toMove = player_current_col +1;
                break;
            case "UP_LEFT": // Move player UP and LEFT
                player_row_toMove = player_current_row -1;
                player_col_toMove = player_current_col -1;
                break;
            case "UP_RIGHT": // Move player UP and RIGHT
                player_row_toMove = player_current_row -1;
                player_col_toMove = player_current_col +1;
                break;
            default: // Don't move player
                break;
        }
        //check location is valid and legal
        if(!maze.checkOnGrid(player_row_toMove, player_col_toMove))
            return;
        else if(!maze.checkCell(player_row_toMove, player_col_toMove))
            return;
        else if(maze.getGoalPosition().getRowIndex() == player_row_toMove && maze.getGoalPosition().getColumnIndex() == player_col_toMove)
            throw new FinishException();
        this.player_current_row = player_row_toMove;
        this.player_current_col = player_col_toMove;
        this.setChanged();
        notifyAllObservers(new int[]{player_current_row, player_current_col});
    }

    public void setStartPlayerPosition(Position start){
        this.player_current_row = start.getRowIndex();
        this.player_current_col = start.getColumnIndex();
    }

    @Override
    public byte[] getCompressedMaze() {return compressed_maze;}

    @Override
    public void loadGame(byte[] data) throws IOException {
        solution =null; //loaded game -- override previous solution
        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(data));
        byte[] decompressedMaze = new byte[1000*1000+24]; //allocating byte[] for the decompressed maze
        is.read(decompressedMaze);
        maze = new Maze(decompressedMaze);
        this.setChanged();
        this.notifyAllObservers(maze);
    }

    @Override
    public void setGameDifficulty(Object level) {
        String difficulty = (String)level;
        if(Configurations.getGenerateAlgorithm().getClass().toString().contains(difficulty))
            return;
        Configurations.setGenerateAlgorithm(difficulty);
        Main.restartServers();
    }
}
