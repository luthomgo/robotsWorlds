package Server.ServerCommands;

import java.io.*;
import java.net.ServerSocket;

public class ServerCommand {
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
                    if (message.equals("quit")){
                        out.println("exit command");
                        break;
                    }
                    else if (message.equals("dump")){
                        out.println("dump command");
                    }
                    else if (message.equals("robots")){
                        out.println("robots command");
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
