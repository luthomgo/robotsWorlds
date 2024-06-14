import Server.Server;

import java.io.IOException;
import java.util.Scanner;

public class RobotWorlds {
    public static void main(String[] args) throws IOException {
        if (args.length >= 1){
            Client.Client.main(args);
        }
        else Server.main(new String[0]);
    }
}
