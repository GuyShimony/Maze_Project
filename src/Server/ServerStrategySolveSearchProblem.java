package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;
import java.util.Dictionary;
import java.util.HashMap;

public class ServerStrategySolveSearchProblem implements IServerStrategy {

    ISearchingAlgorithm algorithm;
    String tempDirectoryPath = System.getProperty("java.io.tmpdir");
    String maze_file_path;
    String solution_file_path;
    HashMap<String,String> MazeID_SolutionFile;


    public ServerStrategySolveSearchProblem() {

        algorithm = Configurations.getSearchingAlgorithm();
       // algorithm = new DepthFirstSearch();
        MazeID_SolutionFile = new HashMap<String, String>();
    }

    /**
     *     The strategy - solve the maze.
     *     Use the handle function from the interface.
     * @param inputStream - the input stream from the user containing the maze.
     * @param outputStream - the output steam to the user. Will be used to send the solution to the user.
     * @throws IOException - IOException and ClassNotFound thrown by the  objectInput/Output stream objects.
     * @throws ClassNotFoundException
     */
    public void handleClient(InputStream inputStream, OutputStream outputStream) throws IOException, ClassNotFoundException {
        /*
         * Get the maze by using the readobject function (casting the inputoutputsteam to Objectinputstream).
         * Turn the maze to searchable maze.
         * Solve the maze by using the solve function and return the solution.
         * Send the solution to the user by using the writeObject from the ObjectOutputstream by casting
         * the outputstream to the ObjectOutputstream.
         * */
        ObjectInputStream objectInput = new ObjectInputStream(inputStream);
        ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);

        Maze maze = (Maze) objectInput.readObject();
        String string_maze = convertMazeToString(maze);
        Solution s;
        // Create the temp file (if does not exist) to save the solutions
        if(checkSolutionExists(string_maze)){
            solution_file_path = MazeID_SolutionFile.get(string_maze);
            //solution_file_path = tempDirectoryPath + String.valueOf(string_maze.hashCode()) +"sol";
            s= (Solution)readObjectFromFile(solution_file_path);
        }
        else {
            SearchableMaze searchableMaze = new SearchableMaze(maze);
            //if solution does not exist, create a new file and write the solution and maze
            s = algorithm.solve(searchableMaze);
          //  WriteToFile(maze_file_path,maze);
            createSolutionFile(string_maze);
            WriteToFile(solution_file_path, s);
        }
        // Send the solution to the client
        objectOutput.writeObject(s);
        outputStream.flush();

    }

    public synchronized void WriteToFile(String path, Object data) throws IOException {

        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(data);
        fileOut.flush();
        fileOut.close();
    }
    public String convertMazeToString(Maze maze){
        String result = "";
        result += maze.getStartPosition() + ",";
        result += maze.getGoalPosition() + "\n";
        int[][] matrix = maze.getMaze();
        for (int i = 0; i < maze.getRows(); i++) {
            for (int j = 0; j < maze.getColumns(); j++) {
                result += matrix[i][j];
            }
            result +="\n";
        }
        return result;
    }
    public boolean checkSolutionExists(String string_maze) throws IOException {

      //  String string_maze = convertMazeToString(maze);
        return MazeID_SolutionFile.containsKey(string_maze);
        //create unique filename with maze's hashcode
      /*  maze_file_path = tempDirectoryPath+ String.valueOf(string_maze.hashCode());
        File file = new File(maze_file_path);
        if(file.createNewFile())//true if file exists
            return false;
        return true;*/
    }
    public synchronized File createSolutionFile(String string_maze) throws IOException {
     //   String string_maze = convertMazeToString(maze);
        //create unique filename with maze's hashcode
        solution_file_path = tempDirectoryPath+ String.valueOf(string_maze.hashCode()) +"sol";
        File file = new File(solution_file_path);
        file.createNewFile();
        MazeID_SolutionFile.put(string_maze,solution_file_path);
        return file;
    }

    public Object readObjectFromFile(String path) throws IOException, ClassNotFoundException {

            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();
            objectIn.close();
            return obj;
    }
}