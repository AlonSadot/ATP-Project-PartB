
package algorithms.mazeGenerators;

public class Maze {
    Position entrance;
    Position exit;
    private int[][] matrix;

    /**
     * @param row - number of the maze's rows
     * @param column- number of the maze's columns
     */
    public Maze(int row, int column) {
        if (row < 2 || column < 2)
            throw new IllegalArgumentException("Invalid Maze Dimensions input");
        matrix = new int[row][column];
    }

    public Maze(byte[] list) {
        int index = 0;
        byte counter = list[0];
        String x = "";
        String y = "";
        while(list[index] != -1){
            x += list[index];
            index++;
        }
        index++;
        while(list[index] != -1){
            y += list[index];
            index++;
        }
        index++;
        int row = Integer.parseInt(x), column = Integer.parseInt(y);
        matrix = new int[row][column];
        x = "";
        y = "";

        while(list[index] != -1){
            x += list[index];
            index++;
        }
        index++;

        while(list[index] != -1){
            y += list[index];
            index++;
        }
        index++;

        entrance = new Position(Integer.parseInt(x),Integer.parseInt(y));
        x="";
        y="";

        while(list[index] != -1){
            x += list[index];
            index++;
        }
        index++;
        while(list[index] != -2){
            y += list[index];
            index++;
        }
        index++;
        exit = new Position(Integer.parseInt(x),Integer.parseInt(y));

        for(int i=0; i< row; i++){
            for (int j=0 ; j< column;j++){
                matrix[i][j] = list[index];
                index++;
            }
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public Position getStartPosition() {
        return entrance;
    }

    public Position getGoalPosition() {
        return exit;
    }

    /**
     * printing function of the 2D maze
     */
    public void print(){
        int[][] temp = matrix;
        temp[entrance.getRowIndex()][entrance.getColumnIndex()]=2;
        temp[exit.getRowIndex()][exit.getColumnIndex()]=3;
        String body = "";
        for (int[] r : matrix){
            body += "{";
            for (int i : r){
                body += " ";
                if (i != 2 && i != 3)
                    body += i;
                else if (i == 2){
                    body += "S";
                }
                else{
                    temp[exit.getRowIndex()][exit.getColumnIndex()]=0;
                    body += "E";
                }
            }
            body += " ";
            body += "}";
            body+="\n";
        }
        System.out.println(body);
    }

    private void addUp(String num, byte[] list, int counter) {
        for (int i = 0; i < num.length(); i++) {
            list[counter+i] = (byte)( num.charAt(i) - '0');
        }
    }

    public byte[] toByteArray() {
        String entranceX = String.valueOf(entrance.x);
        String entranceY = String.valueOf(entrance.y);
        String exitX = String.valueOf(exit.x);
        String exitY = String.valueOf(exit.y);
        String row = String.valueOf(matrix.length);
        String column = String.valueOf(matrix[0].length);
        byte[] list = new byte[matrix.length * matrix[0].length + entranceX.length() + entranceY.length() + exitX.length() + exitY.length() + row.length() + column.length() + 6];
        int counter = 0;

        addUp(row,list,counter);
        counter += row.length();
        list[counter] = -1;
        counter++;

        addUp(column,list,counter);
        counter += column.length();
        list[counter] = -1;
        counter++;

        addUp(entranceX, list, counter);
        counter += entranceX.length();
        list[counter] = -1;
        counter++;

        addUp(entranceY, list, counter);
        counter += entranceY.length();
        list[counter] = -1;
        counter++;

        addUp(exitX, list, counter);
        counter += exitX.length();
        list[counter] = -1;
        counter++;

        addUp(exitY, list, counter);
        counter += exitY.length();
        list[counter] = -2;
        counter++;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                list[counter] = (byte) matrix[i][j];
                counter++;
            }
        }
        return list;
    }


    public static void main(String[] args) {

        IMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(10, 10);

        byte[] list = maze.toByteArray();

        Maze mazi = new Maze(list);

        maze.print();
        mazi.print();
    }


    /*
    // Function below are indicators of the possible neighbors
     */
    public boolean checkUp(Position s){
        if (s.getRowIndex() != 0 && matrix[s.getRowIndex()-1][s.getColumnIndex()]== 0){
            return true;
        }
        return false;
    }

    public boolean checkDown(Position s){
        if (s.getRowIndex() != matrix.length-1 && matrix[s.getRowIndex()+1][s.getColumnIndex()] == 0){
            return true;
        }
        return false;
    }

    public boolean checkLeft(Position s){
        if (s.getColumnIndex() != 0 && (matrix[s.getRowIndex()][s.getColumnIndex()-1] == 0)){
            return true;
        }
        return false;
    }

    public boolean checkRight(Position s){
        if (s.getColumnIndex() != matrix[0].length-1 && matrix[s.getRowIndex()][s.getColumnIndex()+1] == 0){
            return true;
        }
        return false;
    }


    public boolean checkUpLeft(Position s) {
        if ((checkUp(s) || checkLeft(s)) && s.getRowIndex() != 0 && s.getColumnIndex() != 0 &&
                matrix[s.getRowIndex()-1][s.getColumnIndex()-1] == 0) {
            return true;
        }
        return false;
    }

    public boolean checkDownLeft(Position s) {
        if ((checkDown(s) || checkLeft(s)) && s.getRowIndex() != matrix.length-1 && s.getColumnIndex() != 0 &&
                matrix[s.getRowIndex()+1][s.getColumnIndex()-1] == 0) {
            return true;
        }
        return false;
    }

    public boolean checkUpRight(Position s) {
        if ((checkUp(s) || checkRight(s)) && s.getRowIndex() != 0 && s.getColumnIndex() != matrix[0].length-1 &&
                matrix[s.getRowIndex()-1][s.getColumnIndex()+1] == 0) {
            return true;
        }
        return false;
    }

    public boolean checkDownRight(Position s) {
        if ((checkDown(s) || checkRight(s)) && s.getRowIndex() != matrix.length-1 && s.getColumnIndex() != matrix[0].length-1 &&
                matrix[s.getRowIndex()+1][s.getColumnIndex()+1] == 0) {
            return true;
        }
        return false;
    }


}
