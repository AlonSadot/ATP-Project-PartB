package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;
import algorithms.search.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerStrategySolveSearchProblem implements IServerStrategy{

    /**
     * server strategy that gets a maze and returns to the client a solution for said maze
     */
    @Override
    public void ServerStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            Maze maze = (Maze) fromClient.readObject();
            SearchableMaze sMaze = new SearchableMaze(maze);
            byte[] list = maze.toByteArray();
            Solution solved;
            int ones=0;
            for (int i=0 ; i <list.length; i++)
                if (list[i] == 1) ones++;

            OutputStream out = new MyCompressorOutputStream(new FileOutputStream("savedMaze.maze"));
            out.write(list);
            out.flush();
            Path path = Paths.get("savedMaze.maze");

            String savedPath =mazeExists((String.valueOf(ones)),Files.readAllBytes(path));
            if (savedPath != null){
                solved = getSolution(savedPath);
            }
            else{
                Configurations conf = Configurations.getInstance();
                String search = conf.getProperty("mazeSearchingAlgorithm");
                ASearchingAlgorithm searchingAlg;
                if (search.equals("Breadth")) // solves via the algorithm that is written in the configuration file
                    searchingAlg = new BreadthFirstSearch();
                else if (search.equals("DFS"))
                    searchingAlg = new DepthFirstSearch();
                else
                    searchingAlg = new BestFirstSearch();
                solved = searchingAlg.solve(sMaze);
                saveMaze(solved,list,ones);
            }
            toClient.writeObject(solved);
            toClient.flush();
            toClient.close();
            fromClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param solved solution to save
     * @param list byte array
     * @param ones number of ones in the maze
     * saves the maze and his solution
     */
    private static void saveMaze(Solution solved, byte[] list,int ones) {
        try {
            File theDir = new File("Solved_Mazes");
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            String onesName = checkFile(String.valueOf(ones));
            String filename = "Solved_Mazes/" + onesName;
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream("tempMaze.maze"));
            out.write(list);
            out.flush();
            Path p = Paths.get("tempMaze.maze");
            byte[] compressedList = Files.readAllBytes(p);
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                fos.write(compressedList);
            }
            PrintWriter writer = new PrintWriter("Solved_Mazes/" + "Solution_" + onesName + ".txt", "UTF-8");
            writer.println(solved.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param num number of ones in the maze
     * @return returns a string that represents the file name
     */
    private static String checkFile(String num){
        File dir = new File("Solved_Mazes");
        File[] directoryListing = dir.listFiles();
        int count =1;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (cutString(child.getName()).equals(num)){
                    count++;
                }
            }
        } else  return num +"_1";
        return num + "_" + Integer.toString(count);
    }

    /**
     * @param ones amount of ones in the maze
     * @param list byte array that represents a maze
     * @return returns the name of the maze if the one already exists, returns null if not
     * @throws IOException
     */
    private static String mazeExists(String ones ,byte[] list ) throws IOException {
        File dir = new File("Solved_Mazes");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (cutString(child.getName()).equals(ones)){
                    byte[] fileBytes = Files.readAllBytes(Paths.get(child.getPath()));
                    boolean flag = true;
                    for (int i =0; i < fileBytes.length; i++){
                        if (list[i] != fileBytes[i]) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag == true) return child.getName();
                }
            }
        }
        return null;
    }

    /**
     * @param str string that represents a file name
     * @return returns a substring
     */
    private static String cutString(String str){
        int i=0;
        while (str.charAt(i) != '_') i++;
        return str.substring(0,i);
    }

    /**
     * @param path - a path to a file that holdes the solution
     * @return - the solution that is saved in the file
     * @throws IOException
     */
    private static Solution getSolution(String path) throws IOException {
        String mazeText = readFile("Solved_Mazes/" + "Solution_"  + path + ".txt",StandardCharsets.UTF_8);
        ArrayList<AState> solutionPath = new ArrayList<>();
        int k=0 , counter =0;
        String x ="", y="";
        MazeState prevState = null;
        while (mazeText.charAt(k) != ':') k++;
        for (int i=k+1 ; i < mazeText.length(); i++){
            if (mazeText.charAt(i) == '{'){
                x = ""; y = "";
                i++;
                while (mazeText.charAt(i) != ','){
                    x +=mazeText.charAt(i);
                    i++;
                }
                i++;
                while (mazeText.charAt(i) != '}'){
                    y +=mazeText.charAt(i);
                    i++;
                }
                counter ++;
                Position pos = new Position(Integer.parseInt(x),Integer.parseInt(y));
                if (counter == 1)
                    prevState = null;
                MazeState currState = new MazeState(pos,prevState,10);
                prevState = currState;
                solutionPath.add(currState);
            }
        }
        return new Solution(solutionPath);
    }

    /**
     * @param path path to file
     * @param encoding type of encoding
     * @return string that is in the file
     * @throws IOException
     */
    private static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


}


