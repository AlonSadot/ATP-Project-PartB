package Server;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

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
//    private final Logger LOG = LogManager.getLogger(); //Log4j2
    private ExecutorService threadPool; // Thread pool


    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        Configurations conf = Configurations.getInstance();
        String s = conf.getProperty("threadPoolSize");
        this.port = port;
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;
 //        initialize a new fixed thread pool with 2 threads:
        this.threadPool = Executors.newFixedThreadPool(Integer.parseInt(s));
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
                        serverStrategy(clientSocket);
                    });

                } catch (IOException e) {
                    System.out.println("Connection Timed Out");;
                }
                serverSocket.close();
                threadPool.shutdown();
            }
        } catch (IOException e) {
            System.out.println("IOException");
        }
    }


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
       // LOG.info("Stopping server...");
        stop = true;
    }
}
