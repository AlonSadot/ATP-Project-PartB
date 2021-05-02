package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;
import algorithms.search.*;


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
            toClient.writeObject(search.solve(sMaze));
            toClient.flush();
            toClient.close();
            fromClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
