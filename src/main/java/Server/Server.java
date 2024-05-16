package Server;

import Server.Commands.Command;
import Server.Robots.Launch;
import Server.Robots.Robot;
import Server.ServerCommands.ServerCommand;
import Server.World.World;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.*;
import java.net.*;

public class Server
{
    public static World world = new World();

    public static void main(String[] args) throws IOException
    {
        ServerSocket ss = new ServerSocket(5056);
        ServerCommand sc = new ServerCommand();
        sc.ServerCommand(ss);

        while (true)
        {
            Socket s = null;

            try
            {
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread");

                Thread t = new ClientHandler(s, dis, dos);

                t.start();
            }
            catch (Exception e){
                s.close();
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

    }

    @Override
    public void run()
    {
        String received;

        try {
            dos.writeUTF("Server: Welcome to robot worlds what is your name: ");
            received = dis.readUTF();
            dos.writeUTF("Server: Hello " + received);

            dos.writeUTF("Enter launch to start:");
            String request = dis.readUTF();
            System.out.println(request);
            JsonObject jsonObject = JsonParser.parseString(request).getAsJsonObject();

            if (!request.contains("launch"))
            {
                dos.writeUTF("Command not understood");
            }
            else {
            Launch l = new Launch(jsonObject);
            this.robot = l.getRobot();
            Server.world.robotList.add(this.robot);
            JsonObject respond = l.LaunchResponse();
            dos.writeUTF(respond.toString());
            }
            Command command;
            while (true){
                dos.writeUTF("Enter a command");
                String newRequest = dis.readUTF();
                System.out.println(newRequest);
                JsonObject newJsonObject = JsonParser.parseString(newRequest).getAsJsonObject();
                World world = new World();
                command = Command.create(newJsonObject, world);
                JsonObject respond = this.robot.handleCommand(command);
                dos.writeUTF(respond.toString());
                if (request.contains("Exit")) break;}

        }catch (IOException ignored){}

        try
        {
            this.dis.close();
            this.dos.close();

        }catch(IOException ignored){}
    }
}
