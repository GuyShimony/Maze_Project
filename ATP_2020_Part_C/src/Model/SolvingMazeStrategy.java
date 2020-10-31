package Model;

import Client.IClientStrategy;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SolvingMazeStrategy implements IClientStrategy {

    private Solution mazeSolution;
    private Maze maze;

    public SolvingMazeStrategy(Maze maze) {
        this.maze = maze;
    }

    @Override
    public void clientStrategy(InputStream inputStream, OutputStream outputStream) {
        try {
            ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
            ObjectInputStream fromServer = new ObjectInputStream(inputStream);
            toServer.flush();
            toServer.writeObject(maze); //send maze to server
            toServer.flush();
            mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Solution getMazeSolution() {
        return mazeSolution;
    }

    public void setMazeSolution(Solution mazeSolution) {
        this.mazeSolution = mazeSolution;
    }
}

