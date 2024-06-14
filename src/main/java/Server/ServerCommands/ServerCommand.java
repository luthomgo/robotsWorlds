package Server.ServerCommands;


import Server.Robots.Position;
import Server.Robots.Robot;
import Server.Server;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static Server.Server.clientSockets;

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
                        System.out.println(ANSI_BLUE + "========= World Information =========" + ANSI_RESET);
                        System.out.println("Top Left Position: " + ANSI_GREEN +Server.world.getTOP_LEFT() + ANSI_RESET);
                        System.out.println("Bottom Right Position: " + ANSI_GREEN +Server.world.getBOTTOM_RIGHT() + ANSI_RESET);
                        System.out.println("World Visibility: " + ANSI_GREEN +Server.world.getWorldVisibily() + ANSI_RESET);
                        System.out.println("Repair Time: " +  ANSI_GREEN +Server.world.getRepairTime() + ANSI_RESET);
                        System.out.println("Reload Time: " + ANSI_GREEN +Server.world.getReloadTime() + ANSI_RESET);
                        System.out.println("Max Shield: " + ANSI_GREEN +Server.world.getMaxShield() + ANSI_RESET);
                        System.out.println("Max Shots: " + ANSI_GREEN +Server.world.getMaxShots() + ANSI_RESET);
                        System.out.println("Number of Players: " + ANSI_GREEN +Server.world.getPlayers() + ANSI_RESET);

                        System.out.println(ANSI_BLUE+ "========= Obstacles =========" + ANSI_RESET);
                        for (int i = 0; i < Server.world.obstacles.size(); i++) {
                            System.out.println(ANSI_GREEN + (i+1) +ANSI_RESET+ "-> "+ Server.world.obstacles.get(i).toString());
                        }

                        System.out.println(ANSI_BLUE + "========= Robots =========" + ANSI_RESET);
                        for (Robot i : Server.world.robotList) {
                            String name = i.getName();
                            Position pos = i.getPosition();
                            System.out.println("Robot name: "+name +" and is located at "+ANSI_GREEN + pos.toString() + ANSI_RESET);
                        }

                    }
                    else if (message.equals("robots")){
                        System.out.println(ANSI_BLUE + "Robots in the game: "+ ANSI_RESET);
                        for (Robot i : Server.world.robotList) {
                            String name = i.getName();
                            JsonObject state = i.state();
                            System.out.print("Robot Name: ");
                            System.out.println(ANSI_GREEN + name + ANSI_RESET);
                            System.out.println("Robot State: " + state.toString());
                            System.out.println();
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
}
