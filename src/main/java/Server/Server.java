package Server;

import Server.Commands.Command;
import Server.Robots.Launch;
import Server.Robots.Robot;
import Server.ServerCommands.ServerCommand;
import Server.World.World;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import static Server.Commands.Command.*;
/**
 * The main server class responsible for managing client connections and handling commands.
 */
public class Server {

    public static final List<Socket> clientSockets = new ArrayList<>();
    public final static List<String> names = new ArrayList<String>();
    public static World world = new World();
    /**
     * Main method that starts the server and listens for client connections.
     *
     * @param args Command-line arguments (not used).
     * @throws IOException If an I/O error occurs while setting up the server socket.
     */
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5058);
        ServerCommand sc = new ServerCommand();
        sc.ServerCommand(ss);

        while (!ss.isClosed()) {
            Socket s = null;
            try {
                try {
                    s = ss.accept();
                } catch (SocketException ignored) {
                }
                if (s != null) {
                    System.out.println("A new client is connected");

                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    Thread t = new ClientHandler(s, dis, dos);
                    t.start();
                }
            } catch (IOException e) {
                if (s != null) {
                    try {
                        s.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        }
    }
}
/**
 * Handles communication with a single client connected to the server.
 */
class ClientHandler extends Thread
{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    private Robot robot;
    /**
     * Constructs a ClientHandler object for the given client socket.
     *
     * @param s   The client socket.
     * @param dis DataInputStream to receive data from the client.
     * @param dos DataOutputStream to send data to the client.
     */
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        synchronized (Server.clientSockets) {
            Server.clientSockets.add(s);
        }
    }

    @Override
    public void run()
    {
        String received;

        try {
            while (true) {
                dos.writeUTF("Server: Welcome to robot worlds what is your name: ");
                received = dis.readUTF();

                if (Server.names.contains(received)){
                    dos.writeUTF(generateErrorResponse("To many of you in this world").toString());
                }
                else{
                    dos.writeUTF("Server: Hello " + received);
                    Server.names.add(received);
                    break;
                }
            }
            while (true) {
                dos.writeUTF("Enter launch to start (please specify the type):\n-Sniper shield 2 shots 3\n-Tank shield 10 shots 5\n-Brad1 shield 5 shots 10\n-Default shields 6 shots 6");
                String request = dis.readUTF();
                JsonObject jsonObject = JsonParser.parseString(request).getAsJsonObject();

                if (!request.contains("launch")) {
                    dos.writeUTF(generateErrorResponse("Unsupported command").toString());
                } else {
                    if (Server.world.getRobotList().size() == Server.world.getPlayers()){
                        JsonObject toMany = new JsonObject();
                        toMany.addProperty("result","ERROR");
                        JsonObject data = new JsonObject();
                        data.addProperty("message","No more space in this world");
                        toMany.add("data",data);
                        dos.writeUTF(toMany.toString());
                    }else{
                        Launch l = new Launch(jsonObject,s,dos,dis);
                        this.robot = l.getRobot();
                        Server.world.robotList.add(this.robot);
                        JsonObject respond = l.LaunchResponse();
                        dos.writeUTF(respond.toString());
                        break;
                    }
                }
            }
            Command command;
            while (true){
                dos.writeUTF("Enter a command");
                String newRequest = dis.readUTF();
                JsonObject newJsonObject = JsonParser.parseString(newRequest).getAsJsonObject();

                command = Command.create(newJsonObject);
                JsonObject respond = this.robot.handleCommand(command);
                dos.writeUTF(respond.toString());
                if (newRequest.contains("Exit")) break;}

        }catch (IOException ignored){}

        try
        {
            this.dis.close();
            this.dos.close();
        }catch(IOException ignored){}
    }
}
