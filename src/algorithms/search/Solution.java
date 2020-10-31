package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;

public class Solution implements Serializable {

    private ArrayList<AState> path_to_goal;
    public Solution(){path_to_goal = new ArrayList<>();}
    public Solution(ArrayList<AState> path_to_goal) {
        this.path_to_goal = path_to_goal;
    }

    public ArrayList<AState> getSolutionPath() {
        return path_to_goal;
    }
}
