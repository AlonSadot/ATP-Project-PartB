package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;

import java.io.*;
import java.nio.file.*;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);

            int[] mazeDimensions = (int[]) fromClient.readObject();
            AMazeGenerator mmg = new MyMazeGenerator();
            Maze m = mmg.generate(mazeDimensions[0], mazeDimensions[1]);
            String mazeFileName = "savedMaze.maze";
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
            out.write(m.toByteArray());
            out.flush();
            Path path = Paths.get(mazeFileName);
            byte[] mazeCompressed = Files.readAllBytes(path);
            toClient.writeObject(mazeCompressed);
            toClient.flush();
            toClient.close();
            out.close();
            fromClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}