package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HelpCommand extends Command {
    /**
     * A command to display help information about available commands.
     */

    public HelpCommand(JsonArray args) {
        super(args);
    }
    /**
     * Constructs a HelpCommand with the specified arguments.
     *
     * @param args the arguments for the command (not used in this case)
     */

    @Override
    public JsonObject execute(Robot target) {
    /**
     * Executes the help command, providing information about available commands.
     *
     * @param target the robot on which the command is executed
     * @return a JsonObject containing the help information and the current state of the robot
     */

        JsonObject response = new JsonObject();
        response.addProperty("result", "OK");

        JsonObject data = new JsonObject();
        data.addProperty("help", getHelpText());

        response.add("data", data);
        return response;
    }

    private String getHelpText() {
    /**
     * Retrieves the formatted help text with descriptions of available commands.
     *
     * @return a string containing the help text
     */
        return """
                Available Commands:
                1.  state - Returns the current state of the robot.
                2.  look - Returns the current view from the robot's perspective.
                3.  forward <steps> - Moves the robot forward by the specified number of steps.
                4.  back <steps> - Moves the robot backward by the specified number of steps.
                5.  turn <direction> - Turns the robot in the specified direction (left or right).
                6.  help - Displays this help message.
                7.  exit - Exits the program.
                8.  look - Look around in the world
                9.  fire - Instructs the robot to Fires a shot.
                10. reload - Instructs the robot to Reload.
                11. repair - Instructs the robot to Repair.
                """;
    }
}
