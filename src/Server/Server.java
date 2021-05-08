package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private int listeningIntervalMS;
    private IServerStrategy strategy;
    private volatile boolean stop;

    private ExecutorService threadPool;


    /**
     * constructing communication server
     */
    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        Configurations conf = Configurations.getInstance();
        String s = conf.getProperty("threadPoolSize");
        this.port = port;
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;
        this.threadPool = Executors.newFixedThreadPool(Integer.parseInt(s)); // amount of threads that is written in the configuration file
    }

    public void start(){
        new Thread(()->{startServer();}).start();
    } // initializing a server using a single thread


    /**
     * running the server until given a stop order
     */
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client accepted: " + clientSocket.toString());

                    threadPool.submit(() -> { serverStrategy(clientSocket); });
                }
                catch (IOException e) {
                    System.out.println("Connection Timed Out");
                }
            }
            serverSocket.close();
            threadPool.shutdown();
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }


    /**
     * applying the server strategy within the client socket information
     */
    private void serverStrategy(Socket clientSocket) {
        try {
            strategy.ServerStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            System.out.println("Done handling client: " + clientSocket.toString());
            clientSocket.close();
        } catch (IOException e){
            System.out.println("IOException");
        }
    }

    public void stop(){
        System.out.println("Stopping server...");
        stop = true;
    }
}
