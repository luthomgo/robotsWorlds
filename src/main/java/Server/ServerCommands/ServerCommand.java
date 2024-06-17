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

    /**
     * This class represents the server command interface for handling various commands
     * such as quitting the server, dumping world information, and displaying robots in the game.
     */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";

    /**
     * Creates a server command interface that listens for commands from the server admin.
     * Supported commands include:
     * <ul>
     * <li>quit - Disconnects the server and all connected clients.</li>
     * <li>dump - Dumps the current world information, including positions, visibility, repair times, and more.</li>
     * <li>robots - Lists all robots currently in the game along with their state.</li>
     * </ul>
     *
     * @param socket the server socket through which clients are connected
     */
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
                    if (message.equalsIgnoreCase("quit")) {
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
                    else if (message.equalsIgnoreCase("dump")){
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
                    else if (message.equalsIgnoreCase("robots")){
                        System.out.println(ANSI_BLUE + "Robots in the game: "+ ANSI_RESET);
                        for (Robot i : Server.world.robotList) {
                            String name = i.getName();
                            JsonObject state = i.state();
                            StringBuilder formattedResponse = new StringBuilder();

                            System.out.print("Robot Name: ");
                            System.out.println(ANSI_GREEN + name + ANSI_RESET);

                            formattedResponse.append("\nRobot State: ").append(ANSI_RESET).append("\n");
                            state.entrySet().forEach(entry -> {
                                formattedResponse.append(ANSI_YELLOW).append("  ").append(entry.getKey()).append(": ").append(ANSI_RESET).append(entry.getValue().toString()).append("\n");
                            });
                            System.out.println(formattedResponse);
                            System.out.println();
                        }
                    } else if (message.equalsIgnoreCase("help")) {
                        String help =ANSI_CYAN + """
                                        Available Commands:
                                        1.  state - Returns the current state of the robot.
                                        2.  look - Returns the current view from the robot's perspective.
                                        3.  forward <steps> - Moves the robot forward by the specified number of steps.
                                        4.  back <steps> - Moves the robot backward by the specified number of steps.
                                        5.  turn <direction> - Turns the robot in the specified direction (left or right).
                                        6.  help - Displays this help message.
                                        7.  exit - Exits the program.
                                        8.  look - Look around in the world
                                        9.  fire - Instructs the robot to Fires a shot.
                                        10. reload - Instructs the robot to Reload.
                                        11. repair - Instructs the robot to Repair.
                                        """ + ANSI_RESET;
                        System.out.println(help);
                    } else{
                        out.println("command not understood");
                    }
                }
            }
        });
        t.start();
    }
}
