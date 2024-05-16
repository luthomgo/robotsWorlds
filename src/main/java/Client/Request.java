package Client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private String name;
    private String command;
    private String kind;
    private String shield;
    private String shots;

    public Request(String name,String instruction) {
        this.name = name;
        String[] args = instruction.toLowerCase().trim().split(" ");
        this.command = args[0];
//        this.kind = "sniper";
//        this.shield = "1";
//        this.shots = "3";

        if (args.length == 4) {
            this.shots = args[3];
            this.shield = args[2];
            this.kind = args[1];
        }
        else if (args.length == 3){
            this.shots = "6";
            this.shield = args[2];
            this.kind = args[1];
        }

        else if (args.length == 2){
            this.shots = "6";
            this.shield = "3";
            this.kind = args[1];
        }
        else {
            this.shots = "6";
            this.shield = "3";
            this.kind = "tank";
        }

    }

    public JsonObject createRequest(){
        JsonObject request = new JsonObject();
        request.addProperty("robot",this.name);
        request.addProperty("command",this.command);
        List<String> args = new ArrayList<String>();
        args.add(this.kind);
        args.add(this.shield);
        args.add(this.shots);

        JsonArray jsonArray = new JsonArray();
        for (String i:args){
            jsonArray.add(i);
        }
        request.add("arguments", jsonArray);
        return request;
    }
}
