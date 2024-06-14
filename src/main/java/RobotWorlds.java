import Server.Server;

import java.io.IOException;
import java.util.Scanner;

public class RobotWorlds {
    /**
     * Main method that starts the server application.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length >= 1){
            Client.Client.main(args);
        }
        else Server.main(new String[0]);
    }
}
