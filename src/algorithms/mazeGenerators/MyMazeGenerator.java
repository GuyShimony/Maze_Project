package algorithms.mazeGenerators;

import java.util.*;

public class MyMazeGenerator extends AMazeGenerator {

    private Stack<Position> stack;
    private Maze maze;

    public MyMazeGenerator() {
        super();
    }

    @Override
    public Maze generate(int rows, int columns) {

        this.maze = new Maze(rows, columns);
        this.stack = new Stack<>();
        //maze is small
        if(rows<4 && columns<4){
            if(rows<=1 && columns<=1)//only one solution
            {
                maze.setStartPoint();
                maze.setGoalPoint(maze.getStartPosition());
                return maze;
            }
            SimpleMazeGenerator simpleSmall = new SimpleMazeGenerator();
            Maze m = simpleSmall.generate(rows,columns);
            return m;
        }

        maze.setStartPoint();
        // Generate random odd row & column number for the start point
        while (maze.getStartPosition().getRowIndex() % 2 == 0 || maze.getStartPosition().getColumnIndex() % 2 == 0)
            maze.setStartPoint();

        int row = maze.getStartPosition().getRowIndex();
        int col = maze.getStartPosition().getColumnIndex();
        // Fill all other cells with walls - 1
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                maze.FillMaze(i, j, 1);
            }
        }

        maze.FillMaze(row, col, 0);
        // The position to start the algorithm from
        stack.add(maze.getStartPosition());

        /**
         * DFS maze generation algorithm:
         * 1) Start at a random (odd) cell and make it a path (change value to 0). Add it to stack
         * 2) Until the stack is empty:
         *      2.1) Pop a cell from the stack and choose a random neighbor cell.
         *      In this context a neighbor is a position 2 cells ahead in each direction
         *      because 1 cell ahead is considered a wall between the two neighboring cells.
         *      2.2) If the chosen cell is valid add it to stack with the cell in between (break the wall).
         *      Valid means: not out of the grid and not a path.
         *      2.3) If the chosen cell in not valid backtrack 1 step and go back to step 2.2.
         *      2.4) Go back to step 2.
         * 3) Return the Maze
         *
         */
        // The position to start the algorithm from
        stack.add(maze.getStartPosition());
        maze.FillMaze(row, col, 0);
        while (!stack.empty()) {
            Position current = stack.pop();
            row = current.getRowIndex();
            col = current.getColumnIndex();
            maze.FillMaze(row, col, 0);
            // Get all the neighbors of the current Position
            ArrayList<Position> neighbors = pickNeighbors(current);
            // Get a shuffled list of directions
            // This list will be used to pick a neighbor in a random direction
            Integer[] neighbors_directions = randomNeighbors();
            for (int i = 0; i < neighbors.size(); i++) {

                // Pick a random neighbor from the current position
                // And remove it from the neighbor's stack
                Position neighbor = neighbors.remove((int) neighbors_directions[i]);
                neighbors.add((int) neighbors_directions[i], new Position(-9, -9));

                row = neighbor.getRowIndex();
                col = neighbor.getColumnIndex();

                switch (neighbors_directions[i]) {
                    case 0: // Up
                        if ((row < 0 || maze.checkCell(row, col)))
                            continue;
                        else {
                            maze.FillMaze(row + 1, col, 0);
                            maze.FillMaze(row,col,0);
                            stack.push(neighbor);
                            break;
                        }
                    case 1: // Right
                        if (col > maze.getColumns() - 1 || maze.checkCell(row, col))
                            continue;
                        else {
                            maze.FillMaze(row, col - 1, 0);
                            maze.FillMaze(row,col,0);
                            stack.push(neighbor);
                            break;
                        }
                    case 2: // Down
                        if (row > maze.getRows() - 1 || maze.checkCell(row, col))
                            continue;
                        else {
                            maze.FillMaze(row - 1, col, 0);
                            maze.FillMaze(row,col,0);
                            stack.push(neighbor);
                            break;
                        }
                    case 3: // Left
                        if (col < 0 || (maze.checkCell(row, col)))
                            continue;
                        else {
                            maze.FillMaze(row, col + 1, 0);
                            maze.FillMaze(row,col,0);
                            stack.push(neighbor);
                            break;
                        }
                }


            }
        }
        maze.setGoalPoint();
        return maze;
    }

    public Maze getMaze() {
        return maze;
    }

    /**
     * This function will get a Position in the maze and create 4 new Position representing
     * the 4 neighbors in each direction.
     * Remark: in this implementation of the algorithm a neighbor is 2 Positions ahead in each direction and
     * not 1 position ahead.
     * @param p The current Position in the maze the algorithm is in
     * @return List of all the the position's neighbors
     */
    private ArrayList<Position> pickNeighbors (Position p) {
        ArrayList<Position> neighbors = new ArrayList<>();
        int row = p.getRowIndex();
        int col = p.getColumnIndex();
        neighbors.add(new Position(row - 2, col)); // Upper neighbor
        neighbors.add(new Position(row, col + 2)); // Right neighbor
        neighbors.add(new Position(row + 2, col)); // Lower neighbor
        neighbors.add(new Position(row, col - 2)); // Left neighbor

        return neighbors;
    }

    /**
     * This function will create a list of 4 numbers and shuffled the list.
     * Every number represent a direction to go to in the maze. By shuffling the list
     * we can simulate a random direction which the algorithm will move to.
     * @return Array of integers which were shuffled
     */
    private Integer[] randomNeighbors() {
        ArrayList<Integer> randoms = new ArrayList<Integer>();
        randoms.add(0);
        randoms.add(1);
        randoms.add(2);
        randoms.add(3);
        Collections.shuffle(randoms);
        return randoms.toArray(new Integer[4]);
    }

}