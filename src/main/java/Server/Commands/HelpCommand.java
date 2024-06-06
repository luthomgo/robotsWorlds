package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HelpCommand extends Command {

    public HelpCommand(JsonArray args) {
        super(args);
    }

    @Override
    public JsonObject execute(Robot target) {
        JsonObject response = new JsonObject();
        response.addProperty("result", "OK");

        JsonObject data = new JsonObject();
        data.addProperty("help", getHelpText());

        response.add("data", data);
        response.add("state", target.state());

        return response;
    }

    private String getHelpText() {
        StringBuilder helpText = new StringBuilder();
        helpText.append("Available Commands:\n");
        helpText.append("1. state - Returns the current state of the robot.\n");
        helpText.append("2. look - Returns the current view from the robot's perspective.\n");
        helpText.append("3. forward <steps> - Moves the robot forward by the specified number of steps.\n");
        helpText.append("4. turn <direction> - Turns the robot in the specified direction (left or right).\n");
        helpText.append("5. help - Displays this help message.\n");
        return helpText.toString();
    }
}
