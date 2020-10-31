package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator{

    public EmptyMazeGenerator() {
    }


    /**
     * This function will generate n*m sized empty maze (all rows and columns are 0)
     * @param rows
     * @param columns
     * @return new Maze object
     */
    @Override
    public Maze generate(int rows, int columns) {

        Maze maze = new Maze(rows,columns);
        for(int i=0;i<rows;i++){
            for (int j = 0; j < columns; j++) {
                    maze.FillMaze(i,j,0);
            }
        }
        maze.setStartPoint();
        maze.setGoalPoint();
        return maze;
    }
}
