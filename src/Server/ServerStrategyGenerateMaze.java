package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;

import java.io.*;
import java.nio.file.*;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    /**
     * server strategy that generates a maze based on the input dimensions
     * returns a compressed maze in the form of a byte array
     */
    @Override
    public void ServerStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);

            int[] mazeDimensions = (int[]) fromClient.readObject();

            Configurations conf = Configurations.getInstance();
            String mazeGen = conf.getProperty("mazeGeneratingAlgorithm");
            AMazeGenerator mmg;

            if (mazeGen.equals("MyGenerator")) // generates based on the configuration file
                mmg = new MyMazeGenerator();
            else if (mazeGen.equals("Simple"))
                mmg = new SimpleMazeGenerator();
            else
                mmg = new EmptyMazeGenerator();

            Maze m = mmg.generate(mazeDimensions[0], mazeDimensions[1]);
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream("savedMaze.maze"));
            out.write(m.toByteArray());
            out.flush();
            Path path = Paths.get("savedMaze.maze");
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