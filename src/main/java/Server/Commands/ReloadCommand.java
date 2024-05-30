package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ReloadCommand extends Command {

    // Constructor that takes a JsonArray of arguments
    public ReloadCommand(JsonArray args) {
        super(args);
    }

    // The method that gets executed on the target robot
    @Override
    public JsonObject execute(Robot target) {
        JsonObject response = new JsonObject();

        // Reload the robot's shots
        target.reloadShots();

        // Construct the response according to the protocol requirements
        response.addProperty("result", "OK");
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("message", "Done");
        response.add("data", jsonData);



        response.add("state", target.state());

        return response;
    }
}
