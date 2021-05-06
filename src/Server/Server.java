package Server;

import Server.IServerStrategy;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

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
//    private final Logger LOG = LogManager.getLogger(); //Log4j2
    private ExecutorService threadPool; // Thread pool


    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port;
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;
 //        initialize a new fixed thread pool with 2 threads:
        this.threadPool = Executors.newFixedThreadPool(2);
    }

    public void start(){
        new Thread(()->{startServer();}).start();
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
//            LOG.info("Starting server at port = " + port);

            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client accepted: " + clientSocket.toString());

                    new Thread(() -> {
                        ServerStrategy(clientSocket);
                    }).start();

                } catch (IOException e) {
                 //   System.out.println("Connection Timed Out");;
                }
                serverSocket.close();
                threadPool.shutdown();
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }


    private void ServerStrategy(Socket clientSocket) {
        try {
            strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            System.out.println("Done handling client: " + clientSocket.toString());
            clientSocket.close();
        } catch (IOException e){
            System.out.println("IOException");
        }
    }

    public void stop(){
       // LOG.info("Stopping server...");
        stop = true;
    }
}
