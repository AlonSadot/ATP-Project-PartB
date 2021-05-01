package test;

import algorithms.mazeGenerators.*;

public class SelfTestsDELETELATER {

    public static void main(String args[]){
        MyMazeGenerator mazeGen = new MyMazeGenerator();
        Maze maze = mazeGen.generate(1000,1000);
        byte[] arr = maze.toByteArray();
        Maze trymaze = new Maze(arr);
        //trymaze.print();
        System.out.println(trymaze.getStartPosition());
        System.out.println(trymaze.getGoalPosition());
//        String h = "213";
        //byte[] a = h.getBytes();
//        char a = h.charAt(0);
//        int x = a - '0';
//        System.out.println(x);

    }
    public static void ctry(int c){
        c++;
    }

}
