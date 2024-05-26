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

public class Server {
    public static final List<Socket> clientSockets = new ArrayList<>();
    public final static List<String> names = new ArrayList<String>();

    public static World world = new World();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(5055);
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
                    System.out.println("A new client is connected : " + s);

                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    System.out.println("Assigning new thread");

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
class ClientHandler extends Thread
{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    private Robot robot;



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
                    JsonObject error = new JsonObject();
                    error.addProperty("result", "ERROR");
                    JsonObject msg = new JsonObject();
                    msg.addProperty("message", "To many of you in this world");
                    error.add("data", msg);
                    dos.writeUTF(error.toString());
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
                    JsonObject error = new JsonObject();
                    error.addProperty("result", "ERROR");
                    JsonObject msg = new JsonObject();
                    msg.addProperty("message", "Unsupported command");
                    error.add("data", msg);
                    dos.writeUTF(error.toString());
                } else {
                    Launch l = new Launch(jsonObject);
                    this.robot = l.getRobot();
                    Server.world.robotList.add(this.robot);
                    JsonObject respond = l.LaunchResponse();
                    dos.writeUTF(respond.toString());
                    break;
                }
            }
            Command command;
            while (true){
                dos.writeUTF("Enter a command");
                String newRequest = dis.readUTF();
                System.out.println(newRequest);
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
