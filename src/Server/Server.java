package Server;

import Server.IServerStrategy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private int listeningIntervalMS;
    private IServerStrategy strategy;
    private volatile boolean stop;

    private ExecutorService threadPool;


    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port;
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;

        this.threadPool = Executors.newFixedThreadPool(2);
    }

    public void start(){
        new Thread(()->{startServer();}).start();
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client accepted: " + clientSocket.toString());

                    threadPool.submit(() -> {
                        serverStrategy(clientSocket); });
                }
                catch (IOException e) {
                    System.out.println("Connection Timed Out");;
                }
            }
            serverSocket.close();
            threadPool.shutdown();
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }


    private void serverStrategy(Socket clientSocket) {
        try {
            strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
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
