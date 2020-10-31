package algorithms.mazeGenerators;
import java.util.Random;


public class Maze {

    private Position start;
    private Position goal;

    private int rows;
    private int columns;

    private int[][] maze;

    private Random rand;

    public Maze(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.maze = new int[rows][columns];
    }


    /**
     * pick random point to start and end the maze
     * @return start point
     */
    private Position pickRandomPoint()
    {
        rand = new Random();
        int random_row=rand.nextInt(rows);
        int random_col=rand.nextInt(columns);

        return new Position(random_row,random_col);
    }

    /**
     * Check if a cell is set (value is 1)
     * @param row - row cell index in the grid
     * @param column - column cell index in the grid
     * @return true - empty, false - set
     */
    public boolean checkCell(int row, int column){return maze[row][column] == 0;}

    /**
     * Get a Position in the maze represented as row and column indexes
     * and check if the position is part of the maze's grid pane and not
     * accede the maze limits.
     * @param row
     * @param column
     * @return
     */
    public boolean checkOnGrid(int row, int column){
        if(row < 0 || column < 0 || row >= rows || column >= columns)
            return false;

        return true;
    }


    /**
     * Choose a random position in th maze to be set as the entry point.
     */
    public void setStartPoint(){
        this.start = pickRandomPoint();
    }

    /**
     * Choose a random position in th maze to be set as the exit point.
     * If the goal position get picked on the same row or col as the start
     * randomly pick again.
     */
    public void setGoalPoint(){
        this.goal = pickRandomPoint();
        if (rows <= 1 || columns <= 1)
        {
            while(this.goal.getRowIndex() == this.start.getRowIndex() && this.start.getColumnIndex() ==this.goal.getColumnIndex())
                this.goal = pickRandomPoint();
            return;
        }
        else {
            while (!legalGoalPick(goal))
                this.goal = pickRandomPoint();
        }
    }
    private boolean legalGoalPick(Position p){
        int goal_row = p.getRowIndex();
        int goal_col = p.getColumnIndex();
        int start_row = start.getRowIndex();
        int start_col = start.getColumnIndex();

        if(start_row == 0 && goal_row == 0)
            return false;
        else if(start_row == rows - 1 && goal_row == rows -1)
            return false;
        else if(start_col == 0 && goal_col == 0)
            return false;
        else if(start_col == columns - 1 && goal_col == columns - 1)
            return false;
        else if(!checkCell(goal.getRowIndex(),goal.getColumnIndex()))
            return false;
        else if(start_col == goal_col && start_row == goal_row)
            return false;

        return true;
    }
    /**
     * used only for (1,1) case so the goal and start point are the same
     * @param p - Position to be set as an exit point
     */
    public void setGoalPoint(Position p){
        if(p == null)
            return;
        this.goal = p;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int[][] getMaze() {
        return maze;
    }

    /**
     * This function return the start point of the generated maze
     * @return Position - entry point of the maze
     */
    public Position getStartPosition()
    {
        return this.start;
    }

    /**
     * This function return the exit point of the generated maze
     * @return Position - exit point of the maze
     */
    public Position getGoalPosition()
    {
        return this.goal;
    }

    /**
     * This function will get a value and set the cell
     * with the value
     * @param row - cell's row index
     * @param col - cell's column index
     * @param val - {0,1}
     */
    public void FillMaze(int row, int col, int val){
        this.maze[row][col] = val;
    }

    public void print(){
        String maze_display = "";

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                if(i == start.getRowIndex() && j == start.getColumnIndex())
                    maze_display += "S";

                else if(i == goal.getRowIndex() && j == goal.getColumnIndex())
                    maze_display += "E";

                else if(checkCell(i,j))
                    maze_display += "0";

                else
                    maze_display += "1";
            }
            maze_display += "\n";
        }

        // Remove the last /n
        maze_display = maze_display.substring(0,maze_display.length()-1);

        System.out.println(maze_display);
    }


}
