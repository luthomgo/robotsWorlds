package Server.Commands;

import Server.Robots.Robot;
import Server.World.World;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public abstract class Command {
    private JsonArray args;

    public Command(JsonArray args){
        this.args = args;
    }
    public Command(){}

    public abstract JsonObject execute(Robot target);

    public JsonArray getArgument(){
        return this.args;
    }

    public static Command create(JsonObject response) {
        String command = response.get("command").getAsString();
        System.out.println(command);
        JsonArray args = response.get("arguments").getAsJsonArray();
        System.out.println(args);
        return switch (command) {
            case "state" -> new StateCommand();
            case "look" -> new LookCommand();
            case "forward" -> new ForwardCommand(args);
            case "turn" -> new TurnCommand(args);

            default -> throw new IllegalArgumentException("Unsupported command: " + response.toString());
        };
    }
}