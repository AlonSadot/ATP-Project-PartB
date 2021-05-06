package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;
import algorithms.search.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerStrategySolveSearchProblem implements IServerStrategy{

    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);

            Maze maze = (Maze) fromClient.readObject();
            SearchableMaze sMaze = new SearchableMaze(maze);
            String mazeFileName = "savedMaze.maze";
            ASearchingAlgorithm search = new BreadthFirstSearch();
            Solution solved = search.solve(sMaze);
            byte[] list = maze.toByteArray();
            toClient.writeObject(solved);
            toClient.flush();
            toClient.close();
            fromClient.close();
            saveMaze(solved,list);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveMaze(Solution solved, byte[] list) {
        try {

            File theDir = new File("Solved_Mazes");
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            String filename = "Solved_Mazes/test.txt"; // maybe should be input or something
            File savedMaze = new File(filename);
            if (savedMaze.createNewFile()) {
                PrintWriter writer = new PrintWriter(filename, "UTF-8");
                writer.write(solved.toString());
                int x;
                for (int i=0; i < list.length ;i ++){
                    x = list[i];
                    writer.write(String.valueOf(x));
                }
                writer.close();
                System.out.println("File created: " + savedMaze.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
