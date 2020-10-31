package algorithms.search;

import java.util.HashMap;
import java.util.PriorityQueue;

public class BestFirstSearch extends BreadthFirstSearch {

    HashMap<AState,Double> PATH_COST;
    public BestFirstSearch() {
        PATH_COST = new HashMap<>();
        OPEN = new PriorityQueue<>();
    }

    @Override
    public String getName() {
        return "BestFirstSearch";
    }

    /**
     * The Best FS algorithm is greedy and need to choose the edge wit the lowest cost.
     * Each direction has its own cost as defined by the problem.
     * As a result, the algorithm does not want to change the cost of each edge, hence
     * the function does nothing
     * @param current - The current state the algorithm chose
     * @param next_state - The next state the algorithm examine
     *
     */
    @Override
    public boolean Relax(AState current, AState next_state) {
        // IF the next state is unvisited - cost 0 - update the parent and cost
        if (!PATH_COST.containsKey(start))
            PATH_COST.put(current,current.getCost());

        if(!PATH_COST.containsKey(current))
            PATH_COST.put(current,current.getCost());

        double current_cost = PATH_COST.get(current);
        double next_cost = next_state.getCost();

        if(!PATH_COST.containsKey(next_state)) {
            next_state.setCost(current_cost + next_cost);
            next_state.setParent(current);
            PATH_COST.put(next_state,current_cost + next_cost);
            return true;
        }


        if(current_cost+next_cost<PATH_COST.get(next_state)) {
            PATH_COST.put(next_state, current_cost + next_cost);
            next_state.setCost(current_cost + next_cost);
            next_state.setParent(current);
        }

        return false;
    }

}
