package Server.ServerCommands;


import Server.Robots.Position;
import Server.Robots.Robot;
import Server.Server;
import Server.World.SqaureObstacle;
import Server.World.World;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static Server.Server.clientSockets;
import static Server.Server.world;

public class ServerCommand {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public void ServerCommand(ServerSocket socket){
        BufferedReader com = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = new PrintStream(System.out);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){out.println("Enter a command");
                    String message = "";
                    try {
                        message = com.readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (message.equals("quit")) {
                        System.out.println("Disconnecting Server and clients");
                        synchronized (clientSockets) {
                            for (Socket clientSocket : clientSockets) {
                                try {
                                    clientSocket.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            clientSockets.clear();
                            try {
                                socket.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        }
                    }
                    else if (message.equals("dump")){
                        System.out.println(ANSI_BLUE + "Obstacles on world: "+ ANSI_RESET);
                        for (int i = 0; i < Server.world.obstacles.size(); i++) {
                            System.out.println(ANSI_GREEN + (i+1) + "-> "+ Server.world.obstacles.get(i) +ANSI_RESET);
                        }

                        System.out.println(ANSI_BLUE + "Robots name and position: "+ ANSI_RESET);
                        for (Robot i : Server.world.robotList) {
                            String name = i.getName();
                            Position pos = i.getPosition();
                            System.out.println(ANSI_GREEN +"Robot name: "+name +" and is located at "+pos+ ANSI_RESET);
                        }

                    }
                    else if (message.equals("robots")){
                        System.out.println(ANSI_BLUE + "Robots in the game: "+ ANSI_RESET);
                        for (Robot i : Server.world.robotList) {
                            String name = i.getName();
                            JsonObject state = i.state();
                            System.out.print("Robot Name: ");
                            System.out.println(ANSI_GREEN + name + ANSI_RESET); // Print name in green and reset color
                            System.out.println("Robot State: " + state.toString());
                            System.out.println(); // Add an empty line for better readability between robots
                        }


                    }
                    else{
                        out.println("command not understood");
                    }
                }
            }
        });
        t.start();
    }

//    public void robot(){
//        System.out.println("Robot");
//        for(Robot robot : world.getPlayers()){
//            System.out.println("Name: " + robot.getName());
//            System.out.println("Position: " + robot.getPosition());
//            System.out.println("Direction: " + robot.getCurDirection());
//            System.out.println("State: " + robot.getState());
//        }
//
//    }

}
