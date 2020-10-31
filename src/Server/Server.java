package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Server {
    private int port;//The port
    private IServerStrategy serverStrategy;//The strategy for handling clients
    private ExecutorService executer;
    private int listeningTime;
    private volatile boolean stop;
    private Thread server;

    public Server(int port, int listeningInterval , IServerStrategy serverStrategy) {
        executer = Executors.newFixedThreadPool(7);
        this.port = port;
        this.serverStrategy = serverStrategy;
        this.stop = false;
        this.listeningTime = listeningInterval;
    }


    public void run() throws IOException
    {
        try {
            // Start the server on the given port.
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningTime);

            while (!stop) {
                try {
                    /* Wait for a client.
                     * When a client connects, use the right server strategy with the one that
                     * was given during run time.
                     * */
                    Socket clientSocket = serverSocket.accept();
                    userServerStrategy(clientSocket);
                } catch (Exception   e) {
                    //System.out.printf("TimeOut expired, waiting for clients...");
                }
            }
            // Out of the while loop - the server has stopped
            executer.shutdown();
            serverSocket.close();
            System.out.println("server shutting down");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void stop()
    {
        this.stop = true;
    }

    /**
     * Start the new server with a new thread
     */
    public void start(){
        server = new Thread(() -> {
            try {
                run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        server.start();
    }

    /**
     * Handle the client requests with the server strategy that was configured.
     * @param clientSocket - the client that was connected.
     * @throws IOException - thrown by the handleClient function.
     */
    public void userServerStrategy(Socket clientSocket) throws IOException{
        InputStream inFromClient = clientSocket.getInputStream();
        OutputStream outToClient = clientSocket.getOutputStream();
        // Start the client in a new thread in the thread pool
        // By using anonymous class
        executer.execute(()->{
            try {
                this.serverStrategy.handleClient(inFromClient, outToClient);
                // Finished handling the client. Close the connection
                inFromClient.close();
                outToClient.close();
                clientSocket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }



}
