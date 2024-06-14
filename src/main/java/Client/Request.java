package Client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Request {
    /**
     * the Request class represents a request sent from the client to the server.
     * It contains the name of the robot, the command, and the arguments for the command.
     * The arguments are stored as a list of strings.
     * request is a JsonObject that contains the name of the robot, the command, and the arguments for the command.
     * Returns a JsonObject that contains the name of the robot, the command, and the arguments for the command.
     *
     */
    private String name;
    private String command;

    private List<String > arguments;


    public Request(String name,String instruction) {
        this.name = name;
        String[] args = instruction.toLowerCase().trim().split(" ",2);
        this.command = args[0];

        if (args.length >= 2)
        {
        String[] arg = args[1].split(" ");
        this.arguments = Arrays.asList(arg);
        }
        else this.arguments = Collections.emptyList();

    }

    public JsonObject createRequest(){
        JsonObject request = new JsonObject();
        request.addProperty("robot",this.name);
        request.addProperty("command",this.command);

        JsonArray jsonArray = new JsonArray();
        for (String i:this.arguments){
            jsonArray.add(i);
        }
        request.add("arguments", jsonArray);
        return request;
    }
}
