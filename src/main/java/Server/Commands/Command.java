package Server.Commands;

import Server.Robots.Robot;
import Server.World.World;
import com.google.gson.JsonObject;

public abstract class Command {

    public abstract JsonObject execute(Robot target);


    public static Command create(JsonObject response) {
        String command = response.get("command").getAsString();
        System.out.println(command);
        return switch (command) {
            case "state" -> new StateCommand();
            case "look" -> new LookCommand();
            default -> throw new IllegalArgumentException("Unsupported command: " + response.toString());
        };
    }
}

