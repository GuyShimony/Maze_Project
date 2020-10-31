package algorithms.search;

import java.util.ArrayList;

public interface ISearchable {

    public AState getGoalState();
    public AState getStartState();
    public ArrayList<AState> getAllPossibleStates(AState state); //returns neighbors of given state

}
