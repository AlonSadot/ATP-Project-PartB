package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator{

    @Override
    /**
     * @param row - maze's row size
     * @param column - maze's column size
     * @return - returns amount of time it takes to generate the maze
     */
    public long measureAlgorithmTimeMillis(int row, int column) { // measures maze construction time
        long startTime = System.currentTimeMillis();
        generate(row,column);
        long stopTime  = System.currentTimeMillis();
        return stopTime - startTime;
    }
}
