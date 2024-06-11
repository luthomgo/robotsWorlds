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
        return """
                Available Commands:
                1. state - Returns the current state of the robot.
                2. look - Returns the current view from the robot's perspective.
                3. forward <steps> - Moves the robot forward by the specified number of steps.
                4. back <steps> - Moves the robot backward by the specified number of steps.
                5. turn <direction> - Turns the robot in the specified direction (left or right).
                6. help - Displays this help message.
                7. exit - Exits the program.
                8. look - Look around in the world
                9. fire - Instructs the robot to Fires a shot.
                10.reload - Instructs the robot to Reload.
                11. repair - Instructs the robot to Repair.
                """;
    }
}
