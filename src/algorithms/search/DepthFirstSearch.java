package algorithms.search;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Stack;

public class DepthFirstSearch extends ASearchingAlgorithm {

    private Stack<AState> path;
    private Random random;
    //fast path is used for faster search on the stack holding the same states
    private Hashtable<Integer, AState> fast_path;

    public DepthFirstSearch() {
        path = new Stack<>();
        fast_path = new Hashtable<Integer, AState>();
        random = new Random();
    }

    @Override
    public String getName() {
        return "DepthFirstSearch";
    }

    @Override
    public Solution solve(ISearchable problem) {
        if(problem == null)
            return new Solution();

        AState start_state = problem.getStartState();
        AState goal_state = problem.getGoalState();
        if(goal_state == start_state)
            return OneOnOne(problem);
        Hashtable<Integer,AState> visited_states = new Hashtable<>();
        /**
         * DFS Algorithm:
         * 1. Push the start state to stack
         * 2. Until the stack is not empty:
         *  2.1 Pop the state from the top of the stack
         *     * Order does not matter
         *     2.1.1 Check if the neighbors were not visited
         *     2.1.2 For each unvisited neighbor push it to the stack
         *  2.2 If the state that was popped is the goal state return the solution
         *  Else return Null
         *
         */
        fast_path.put((Integer)start_state.hashCode(),start_state);
        path.add(start_state);
        visited_states.put((Integer)start_state.hashCode(),start_state);
        AState current;
        ArrayList<AState> neighbors;
        // Stage 2 of the algorithms
        while (!path.empty()){
            // Increment the number of nodes the algorithm checks
            number_of_nodes_evaluated++;
            current = path.pop();
            fast_path.remove(current.hashCode());
            // Stage 2.1.1
            if(!visited_states.containsKey((Integer)current.hashCode()))
                visited_states.put((Integer)current.hashCode(),current);
            // Get all the valid neighbors of current
            neighbors = (ArrayList<AState>)problem.getAllPossibleStates(current).clone();
            int neighbors_size = neighbors.size();
            int size;
            AState neighbor;
            for (int i = 0; i < neighbors_size; i++) {
                size = neighbors.size();
                // randomly add the neighbors to the stack
                neighbor = neighbors.remove(random.nextInt(size));
                //if the neighbor was already visited or added -- continue
                if(visited_states.containsKey((Integer)neighbor.hashCode()) || fast_path.containsKey((Integer)neighbor.hashCode()))
                    continue;
                neighbor.setParent(current);
                // Push the neighbors in a random order to the stack
                fast_path.put((Integer)neighbor.hashCode(),neighbor);
                path.add(neighbor);
                // If the goal state was popped return the solution
                if(neighbor.equals(goal_state)) {
                    /* Clear the Data structures to prevent race condition*/
                    path.clear();
                    fast_path.clear();
                    /* ***********************************************/
                    return traceBack(neighbor);
                }
            }

        }

        // No solution to the maze return an empty solution
        return new Solution();
    }



}
