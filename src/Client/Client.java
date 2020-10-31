package Client;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private IClientStrategy clientStrategy;
    private int port;
    private InetAddress serverIP;

    public Client(InetAddress serverIP, int port,IClientStrategy clientStrategy) {
        this.clientStrategy = clientStrategy;
        this.port = port;
        this.serverIP = serverIP;
    }

    public void communicateWithServer()
    {
        try {
            Socket socket = new Socket(serverIP,port);
            //System.out.println("Client is connected to server!");
            clientStrategy.clientStrategy(socket.getInputStream(),socket.getOutputStream());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}