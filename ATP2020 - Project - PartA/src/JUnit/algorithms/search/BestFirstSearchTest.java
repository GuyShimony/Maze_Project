package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class BestFirstSearchTest extends BreadthFirstSearch {

    private BestFirstSearch bfs = new BestFirstSearch();
    private DepthFirstSearch dfs = new DepthFirstSearch();
    private MyMazeGenerator mg = new MyMazeGenerator();

    @Test
    void testGetName() {
        assertEquals("BestFirstSearch",bfs.getName());
    }

    @Test
    void relax() {
    }

    @Test
    void smallGraph(){
        Maze m = mg.generate(1000,1000);
        //assertFalse(m.getStartPosition()==m.getGoalPosition());
        SearchableMaze sm = new SearchableMaze(m);
      //  Solution s = bfs.solve(sm);
       // m.print();
       // assertNotNull(s.getSolutionPath());
        //assertNotEquals(0,bfs.getNumberOfNodesEvaluated());

      //  System.out.printf(String.format("eden is gay: %s",mg.measureAlgorithmTimeMillis(1000,1000)));
        System.out.printf(String.format("BEST : %s \n",bfs.measureAlgorithmTimeMillis(sm)));
        System.out.printf(String.format("DFS : %s",dfs.measureAlgorithmTimeMillis(sm)));

    }
}