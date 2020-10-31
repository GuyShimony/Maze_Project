package algorithms.mazeGenerators;

import java.util.Random;

public abstract class AMazeGenerator implements IMazeGenerator {

    protected Maze maze;
    protected Random randomGenerator;

    public AMazeGenerator() {this.randomGenerator = new Random();}

    public abstract Maze generate(int rows, int columns);


    /**
     * This function will be used to measure the algorithms running time
     * @param rows
     * @param columns
     * @return -> Long - the time it took by algorithm to create the maze.
     */
    @Override
    public long measureAlgorithmTimeMillis(int rows, int columns) {
        long start = System.currentTimeMillis();
        generate(rows, columns);
        long end = System.currentTimeMillis();

        return end - start;

    }
}
