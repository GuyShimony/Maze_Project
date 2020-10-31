package algorithms.mazeGenerators;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.RunnableScheduledFuture;


public class Maze implements Serializable {

    private Position start;
    private Position goal;

    private int rows;
    private int columns;

    private int[][] maze;
    private Random rand;

    private volatile int place;

    public Maze(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.maze = new int[rows][columns];
    }

    /**
     * constructor: initialize the maze based on byte array data.
     * byte array should include: dimension, start position, goal position, maze content(in that order)
     * @param b - byte array including the maze data
     */
    public Maze(byte[] b){
        place =0;
        //set the dimensions
        columns  = getNextValue(b);
        rows = getNextValue(b);
        this.maze = new int[rows][columns];
        //set start position
        int start_rows = getNextValue(b);
        int start_cols = getNextValue(b);
        start  = new Position(start_rows,start_cols);
        //System.out.printf("start "+start.toString());

        //set goal position
        int goal_rows = getNextValue(b);
        int goal_cols = getNextValue(b);
        goal  = new Position(goal_rows,goal_cols);
       // System.out.printf("goal "+goal.toString());

        //set maze
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                maze[i][j] = getNextValue(b);
            }
        }
    }

    /**
     * use to get the integer's unsigned value from each space in the byte array
     * when we reach a number larger than 254 we calculate its value by checking the next slots(for each number larger
     * than 254 we provide more than 1 slot).
     * when we reach a value smaller than 255 we add to the value and return the final outcome
     * @param b - byte array to read from
     * @return unsigned value of the next number
     */
    public int getNextValue(byte[] b){
        int value=0;
        int unsigned = b[place] & 0xFF;
        while(unsigned >= 255){
            value+=unsigned;
            place++;
            unsigned = b[place] & 0xFF;
        }
        value+=unsigned;
        place++;
        return value;
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

        if(!checkCell(goal_row,goal_col))
            return false;
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

    /**
     * convert all the metadata of the maze to bytes: dimensions, start position, goal position, maze content
     * @return byte array representing the metadata of the maze
     */
    public byte[] toByteArray(){

        ArrayList<Byte> temp = new ArrayList<>();

        // Convert maze dimensions to bytes
        checkIntValueAndConvertToByte(temp,columns);
        checkIntValueAndConvertToByte(temp,rows);

        // Convert Start position to bytes
        checkIntValueAndConvertToByte(temp,start.getRowIndex());
        checkIntValueAndConvertToByte(temp,start.getColumnIndex());

        // Convert Goal position to bytes
        checkIntValueAndConvertToByte(temp,goal.getRowIndex());
        checkIntValueAndConvertToByte(temp,goal.getColumnIndex());

        // Convert the maze to bytes
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                temp.add(convertIntToByte(maze[i][j]));
            }
        }

        // Convert arraylist to array
        byte[] result = new byte[temp.size()];
        for (int i = 0; i < temp.size(); i++)
            result[i] = temp.get(i);


        return result;

    }

    /**
     * Use ByteArrayOutput object to convert the int to an unassigned byte
     * @param x - the integer to convert
     * @return byte object of x - unassigned
     */
    public byte convertIntToByte(int x){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            bos.write(x);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bos.toByteArray()[0];
    }

    /**
     * Used to handle int bigger then 255, meaning they take more than one byte.
     * The function will decrease 255 from the original number and add it to the byte array.
     * When the original size will be smaller than 255, the last value will be added to the byte array. It can be
     * represented in a single byte.
     * @param result - the Array of bytes to add bytes to
     * @param val - the int which is bigger than 255.
     */
    private void convertBigIntToBytes(ArrayList<Byte> result, int val){
        result.add(convertIntToByte(255));
        val -= 255;
        while (val >= 255){
            result.add(convertIntToByte(255));
            val -= 255;
        }
        result.add(convertIntToByte(val));
    }

    /**
     * Wrapper function for the if statement checking the value of the int
     * and triggering the right action based on the value.
     * If bigger than 255 -> use the convertBigIntToBytes function
     * @param x - the integer to check.
     */
    private void checkIntValueAndConvertToByte(ArrayList<Byte> result,int x){
        if(x >= 255)
            convertBigIntToBytes(result,x);
        else
            result.add(convertIntToByte(x));
    }




}
