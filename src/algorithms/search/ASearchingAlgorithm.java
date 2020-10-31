package algorithms.search;

import java.util.ArrayList;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {

    Solution solution;
    protected int number_of_nodes_evaluated;

    public ASearchingAlgorithm() {
        number_of_nodes_evaluated = 0;
    }


    public long measureAlgorithmTimeMillis(ISearchable prob) {
        long start = System.currentTimeMillis();
        solve(prob);
        long end = System.currentTimeMillis();

        return end - start;

    }


    public abstract String getName();
    public int getNumberOfNodesEvaluated(){return number_of_nodes_evaluated;};

    protected Solution OneOnOne(ISearchable problem){
        ArrayList<AState> one = new ArrayList<>();
        one.add(problem.getGoalState());
        number_of_nodes_evaluated++;
        return new Solution(one);
    }
    /**
     *
     * @param end_state - the goal state as states by the algorithm
     * @return array consisting of the path from the start state to the goal state
     */
    public Solution traceBack(AState end_state){
        if(end_state ==null)
            return null;
        ArrayList<AState> temp = new ArrayList<AState>();
        // Add the goal state first
        temp.add(end_state);
        // Traceback to the start state
        while(end_state.getParent()!=null) {
            temp.add(end_state.getParent());
            end_state = end_state.getParent();
        }
        ArrayList<AState> path = new ArrayList<AState>();
        for (int i = temp.size()-1; i >= 0  ; i--) {
            path.add(temp.remove(i));
        }
        solution = new Solution(path);
        return solution;
    }
}
