package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;

public class Solution implements Serializable {
    private ArrayList<AState> path;

    /**
     * @param path - the path of the solution
     */
    public Solution(ArrayList<AState> path) {
        if (path == null)
            throw new IllegalArgumentException("Invalid Input");
        this.path = path;
    }
    public ArrayList<AState> getSolutionPath(){
        return path;
    }
}
