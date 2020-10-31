package algorithms.mazeGenerators;

public class SimpleMazeGenerator extends AMazeGenerator {

    public SimpleMazeGenerator() {super();}

    @Override
    public Maze generate(int rows, int columns) {
        maze = new Maze(rows, columns);
        maze.setStartPoint();
        maze.setGoalPoint();
        int start_row = maze.getStartPosition().getRowIndex();
        int start_col = maze.getStartPosition().getColumnIndex();
        int goal_row = maze.getGoalPosition().getRowIndex();
        int goal_col = maze.getGoalPosition().getColumnIndex();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                maze.FillMaze(i, j, 1);
            }
        }

        if (start_col == goal_col && start_row < goal_row)
            for (int i = start_row; i <= goal_row; i++) {
                maze.FillMaze(i, start_col, 0);
            }
        else if (start_col == goal_col && start_row > goal_row) {
            for (int i = goal_row; i <= start_row; i++) {
                maze.FillMaze(i, start_col, 0);
            }
        } else if (start_row == goal_row && start_col < goal_col)
            for (int i = start_col; i <= goal_col; i++) {
                maze.FillMaze(start_row, i, 0);
            }
        else if (start_row == goal_row && start_col > goal_col)
            for (int i = goal_col; i <= start_col; i++) {
                maze.FillMaze(start_row, i, 0);
            }
        else {
            if (start_row < goal_row && start_col < goal_col) {
                for (int i = start_row; i <= goal_row; i++) {
                    maze.FillMaze(i, start_col, 0);
                }
                for (int i = start_col; i <= goal_col; i++) {
                    maze.FillMaze(start_row, i, 0);
                }
            }

            else if(start_row < goal_row && start_col > goal_col){
                for (int i = start_row; i <= goal_row; i++) {
                    maze.FillMaze(i, start_col, 0);
                }
                for (int i = goal_col; i <= start_col; i++) {
                    maze.FillMaze(goal_row, i, 0);
                }
            }
            else if (start_row > goal_row && start_col < goal_col) {
                for (int i = goal_row; i <= start_row; i++) {
                    maze.FillMaze(i, goal_col, 0);
                }
                for (int i = start_col; i <= goal_col; i++) {
                    maze.FillMaze(start_row, i, 0);
                }
            }
            else if (start_row > goal_row && start_col > goal_col) {
                for (int i = goal_row; i <= start_row; i++) {
                    maze.FillMaze(i, goal_col, 0);
                }
                for (int i = goal_col; i <= start_col; i++) {
                    maze.FillMaze(start_row, i, 0);
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i != start_row && j != start_col && !maze.checkCell(i, j))
                    maze.FillMaze(i, j, randomGenerator.nextInt(2));
            }
        }
        return maze;
    }
}
