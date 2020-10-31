package algorithms.search;

public interface ISearchingAlgorithm {

    public Solution solve(ISearchable problem);
    public String getName();
    public int getNumberOfNodesEvaluated();

}
