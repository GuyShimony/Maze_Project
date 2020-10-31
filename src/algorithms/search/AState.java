package algorithms.search;

import java.io.Serializable;
import java.util.Objects;

public abstract class AState implements Comparable<AState>, Serializable {

    private AState parent;
    private Object location;
    private double cost;

    public AState(Object location, AState parent, double cost) {
        this.parent = parent;
        this.location = location;
        this.cost = cost;
    }

    public AState(Object location,double cost) {
        this.location = location;
        this.cost = cost;
        this.parent = null;
    }

    public void setParent(AState parent) {
        this.parent = parent;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public AState getParent() {
        return parent;
    }

    public double getCost() {
        return cost;
    }

    public Object getLocation() {
        return location;
    }

    /**
     *
     * @param o
     * @return 1 if this is bigger -1 if para is bigger and 0 if equal
     */
    @Override
    public int compareTo(AState o) {
        if(this.cost > o.cost)
            return 1;
        else if(this.cost < o.cost)
            return -1;
        return 0;
    }


    @Override
    public int hashCode() {
        return Objects.hash(location.toString());
    }
}
