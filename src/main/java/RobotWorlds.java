import Server.Server;

import java.io.IOException;

/**
 * Main class for starting the Robot Worlds application.
 *
 * This class determines whether to start the client or server based on command-line arguments.
 */
public class RobotWorlds {
    /**
     * Main method that starts the Robot Worlds application.
     *
     * If command-line arguments are provided, it starts the client application using {@link Client.Client#main(String[])}.
     * If no arguments are provided, it starts the server application using {@link Server#main(String[])}.
     *
     * @param args Command-line arguments. If at least one argument is provided, it is assumed to be for the client.
     * @throws IOException If an I/O error occurs during client-server communication.
     */
    public static void main(String[] args) throws IOException {
        if (args.length >= 1){
            Client.Client.main(args);
        }
        else {
            Server.main(new String[0]);
        }
    }
}
