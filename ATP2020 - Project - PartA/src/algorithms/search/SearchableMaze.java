package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;



import java.util.ArrayList;
import java.util.HashMap;


public class SearchableMaze implements ISearchable {

    private Maze maze;
    private AState[][] states_matrix;
    private HashMap<String,ArrayList<AState>> adjacencyListMap;
    public SearchableMaze(Maze maze) {
        this.maze = maze;
        adjacencyListMap = new HashMap<>();
        states_matrix = new AState[maze.getRows()][maze.getColumns()];
        createStatesMaze();
        createGraph();
    }

    /**
     * Get a maze and turn each cell into an MazeState Object
     * to be later turned into a graph
     */
    private void createStatesMaze() {
        for (int i = 0; i < maze.getRows();i++) {
            for (int j = 0; j <maze.getColumns() ; j++) {
                states_matrix[i][j] = new MazeState(new Position(i,j),0);
            }
        }
    }

    /**
     * The function will get a states matrix and turn it to a graph
     * represented by adjacency list
     */
    private void createGraph() {
        for (int i = 0; i < maze.getRows(); i++) {
            for (int j = 0; j < maze.getColumns(); j++) {
                // If the position is a path
                if(maze.checkCell(i,j)){
                    String key = String.valueOf(i) +"," + String.valueOf(j);
                    adjacencyListMap.put(key,null);
                    addEdges(i,j);
                }
            }
        }
    }

    /**
     * Gets all the neighbors of a node from the adjacency list
     * @param state - the state to get all the of its neighbors
     * @return Array of AStates representing the neighbors of the state given
     */
    @Override
    public ArrayList<AState> getAllPossibleStates(AState state)
    {
        Position p = ((MazeState) state).getPosition();
        String key = String.valueOf(p.getRowIndex()) + "," + String.valueOf(p.getColumnIndex());
        return adjacencyListMap.get(key);
    }


    @Override
    public AState getGoalState() {
        //cost will be updated during neighbors return
        return states_matrix[maze.getGoalPosition().getRowIndex()][maze.getGoalPosition().getColumnIndex()];
    }

    @Override
    public AState getStartState() {
        return states_matrix[maze.getStartPosition().getRowIndex()][maze.getStartPosition().getColumnIndex()];
    }

    /**
     * Given a row and col check if the position is legal based on the rules
     * that were set in the maze project.
     * Use checkOnGrid(int, int) & checkCell(int, int) function of the maze
     * @param row - row index in the maze
     * @param col - col index in the maze
     * @return true if the position is legal, false if not
     */
    private boolean isLegal(int row, int col) {
        return maze.checkOnGrid(row,col) && maze.checkCell(row,col);
    }

    /**
     * The function will get a node (represented by a position) on the graph
     * and add all of the neighbors, that are paths (meaning zeros), to the adjacency List
     * of that node.
     * @param row - node row index on the matrix
     * @param col - node col index on the matrix
     */
    public void addEdges(int row, int col) {

        String key = String.valueOf(row) +"," + String.valueOf(col);
        ArrayList<AState> neighbors = new ArrayList<>();

        /* Get the neighbors in a clockwise manner
         * For all the diagonal neighbors need to check two position.
         * */
        boolean up_right_flag = false;
        boolean up_left_flag = false;
        boolean down_right_flag = false;
        boolean down_left_flag = false;
        // Upper side neighbor & Upper right diagonal side neighbor
        if (isLegal(row -1, col)) {
            // Upper
            neighbors.add(new MazeState(new Position(row -1,col),10));
            if (isLegal(row - 1, col + 1)) {
                // Diagonal Up Right
                neighbors.add(new MazeState(new Position(row - 1, col + 1),15));
                up_right_flag = true;
            }
            if (isLegal(row - 1, col - 1)) {
                // Diagonal Up Left
                neighbors.add(new MazeState(new Position(row - 1, col - 1),15));
                up_left_flag = true;
            }
        }

        if (isLegal(row, col + 1)) {
            // Right
            neighbors.add(new MazeState(new Position(row, col + 1),10));
            // Up Right Diagonal and hasn't been added yet
            if (!up_right_flag && isLegal(row - 1, col + 1))
                neighbors.add(new MazeState(new Position(row - 1, col + 1),15));
            // Down Right Diagonal
            if(isLegal(row+1, col+1)) {
                neighbors.add(new MazeState(new Position(row+1, col+1),15));
                down_right_flag = true;
            }
        }

        //Down side neighbor & Down right diagonal side neighbor
        if (isLegal(row+1, col)) {
            // Down side
            neighbors.add(new MazeState(new Position(row+1, col),10));
            if(!down_right_flag && isLegal(row + 1, col + 1)){
                // Diagonal Down Right
                neighbors.add(new MazeState(new Position(row + 1, col + 1),15));
                down_right_flag = true;
            }

            if (isLegal(row+1, col-1)) {
                // Diagonal Down Left
                neighbors.add(new MazeState(new Position(row+1, col-1),15));
                down_left_flag = true;
            }
        }

        //Down Left diagonal side neighbor & Left side neighbor
        if (isLegal(row, col-1)) {
            // Left
            neighbors.add(new MazeState(new Position(row, col-1),10));
            if (!down_left_flag && isLegal(row + 1, col - 1)) {
                // Diagonal Down Left
                neighbors.add(new MazeState(new Position(row + 1, col - 1),15));
            }
            if (!up_left_flag && isLegal(row - 1, col - 1)) {
                // Diagonal Up Left
                neighbors.add(new MazeState(new Position(row - 1, col - 1),15));
            }
        }
        adjacencyListMap.put(key,neighbors);
    }


}
