package algorithms.search;

import algorithms.mazeGenerators.Position;

public class MazeState extends AState {

    private Position position;

    public MazeState(Position location, AState parent, double cost) {
        super((Object)location, parent, cost);
        position = location;
    }

    public MazeState(Position location,double cost) {
        super((Object)location,cost);
        position = location;
    }

    @Override
    public String toString() {
        return position.toString();
    }

    public Position getPosition() {
        return position;
    }

    /**
    *
    * @param obj - MazeState to compare to
    * @return comparison by location (same cell)
    */
   @Override
    public boolean equals(Object obj) {
       if(obj == null)
           return false;
       int obj_row = (( (MazeState) obj).position.getRowIndex());
       int obj_col = (( (MazeState) obj).position.getColumnIndex());
       int this_row = this.position.getRowIndex();
       int this_col =this.position.getColumnIndex();
       return obj_row == this_row && obj_col == this_col;
  }




}
