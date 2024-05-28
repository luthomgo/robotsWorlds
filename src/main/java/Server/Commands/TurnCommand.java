package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TurnCommand extends Command {

    public TurnCommand(JsonArray args) {
        super(args);
    }

    @Override
    public JsonObject execute(Robot target) {
        // Retrieve the turn direction argument
        JsonArray arguments = getArgument();
        if (arguments.size() == 0) {
            throw new IllegalArgumentException("No arguments provided for turn command");
        }

        String turnDirection = arguments.get(0).getAsString();

        // Create the JSON response object
        JsonObject response = new JsonObject();

        if (turnDirection.equalsIgnoreCase("right")) {
            // Update the robot's direction to the right
            target.updateDirection(true);

            // Populate response data
            JsonObject data = new JsonObject();
            response.addProperty("result", "ok");
            JsonObject jsondata = new JsonObject();
            jsondata.addProperty("message", "done");
            response.add("data",jsondata);
            response.add("state", target.state());

        } else if (turnDirection.equalsIgnoreCase("left")) {
            // Update the robot's direction to the right
            target.updateDirection(true);

            // Populate response data
            JsonObject data = new JsonObject();
            response.addProperty("result", "ok");
            JsonObject jsondata = new JsonObject();
            jsondata.addProperty("message", "done");
            response.add("data",jsondata);
            response.add("state", target.state());

        } else {
            throw new IllegalArgumentException("Invalid turn direction: " + turnDirection);
        }

        return response;
    }
}
