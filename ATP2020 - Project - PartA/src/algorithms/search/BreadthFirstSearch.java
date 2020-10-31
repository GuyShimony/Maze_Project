package algorithms.search;

import java.util.*;

public class BreadthFirstSearch extends ASearchingAlgorithm{

    protected Queue<AState> OPEN;
    private Hashtable<Integer, AState> closed;
    protected AState start;
    protected AState goal;

    public BreadthFirstSearch() {
        OPEN = new LinkedList<>();
        closed = new Hashtable();
    }

    @Override
    public String getName() {
        return "BreadthFirstSearch";
    }

    @Override
    public Solution solve(ISearchable problem) {
        if(problem == null)
            return new Solution();

        start = problem.getStartState();
        goal = problem.getGoalState();
        if(start == goal)
            return OneOnOne(problem);

        OPEN.add(start);
        while (!OPEN.isEmpty()){
            // Increment the number of nodes the algorithm checks
            number_of_nodes_evaluated++;
            AState currentState = OPEN.poll();

            for (AState s: problem.getAllPossibleStates(currentState)) {
                // If the neighbor is in CLOSED -> continue to check other neighbors
                if(closed.containsKey((Integer)s.hashCode()))
                    continue;

                if(Relax(currentState,s))
                    OPEN.add(s); //if the neighbor was not visited yet -- add it to OPEN
                if(s.equals(goal))
                    return traceBack(s); //return the goal state now including all the parents to trace back
                }
            if(!closed.containsKey((Integer)currentState.hashCode()))
                closed.put((Integer)currentState.hashCode(),currentState);
            }

            // No solution to the maze return an empty solution
            return new Solution();
        }

    /**
     * set neighbor's parent to be current if the neighbor was not already visited
     * @param current - current node in the OPEN queue
     * @param next_state - current cell's neighbor
     * @return true - neighbor was not visited yet , false - neighbor was already visited
     */
    public boolean Relax(AState current, AState next_state){
        if(!OPEN.contains(next_state)){
            next_state.setParent(current);
            return true;
        }
        return false;
    }
}
