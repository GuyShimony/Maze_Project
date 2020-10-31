package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    IMazeGenerator generator;
    MyCompressorOutputStream compressor;

    public ServerStrategyGenerateMaze() {

        generator = Configurations.getGenerateAlgorithm();
        System.out.println(generator.getClass());
        //generator = new MyMazeGenerator();
    }

    @Override
    public void handleClient(InputStream inputStream, OutputStream outputStream) throws IOException, ClassNotFoundException{
        try {
            // Cast the input stream to object input stream to get the int[]
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            // Create a temp byte array to read the data from the input stream.
            // Copy the data to a new int type
            int[] dimensions  = (int[]) objectInputStream.readObject();
            int rows = dimensions[0];
            int columns = dimensions[1];
            // Create the new maze with the generating algorithm
            Maze maze = generator.generate(rows,columns);
            // Compress the maze and sent it to the client
            compressor = new MyCompressorOutputStream(objectOutputStream);
            compressor.writeCompressedMaze(maze.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
