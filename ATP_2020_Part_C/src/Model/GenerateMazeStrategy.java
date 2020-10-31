package Model;

import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;

import java.io.*;

public class GenerateMazeStrategy implements IClientStrategy {

    private int[] mazeDimensions;
    private Maze maze;
    private byte[] compressed_maze;

    public GenerateMazeStrategy(int rows, int columns) {
        this.mazeDimensions = new int[]{rows, columns};
    }

    @Override
    public void clientStrategy(InputStream inputStream, OutputStream outputStream) {
        try {
            ObjectOutputStream toServer = new ObjectOutputStream(outputStream);
            ObjectInputStream fromServer = new ObjectInputStream(inputStream);
            toServer.flush();
            toServer.writeObject(mazeDimensions); //send maze dimensions to server
            toServer.flush();
            this.compressed_maze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressed_maze));
            byte[] decompressedMaze = new byte[mazeDimensions[0]*mazeDimensions[1]+24 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
            is.read(decompressedMaze); //Fill decompressedMaze with bytes
            this.maze = new Maze(decompressedMaze);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Maze getMaze() {
        return maze;
    }

    public byte[] getCompressed_maze() {
        return compressed_maze;
    }
}
