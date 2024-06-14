package Server.Commands;

import Server.Robots.*;
import com.google.gson.JsonObject;

public class ReloadCommand extends Command {
/**
 * The ReloadCommand class represents a command to reload a robot's ammunition.
 * It extends the abstract Command class and provides the implementation for the execute method.
 */


    @Override
    public JsonObject execute(Robot target) {
    /**
     * Executes the reload command on the specified target robot.
     * If the robot is not already reloading, it sets the robot's status to "RELOAD"
     * and simulates reload time with a sleep.
     * Once reloaded, it sets the robot's status back to "NORMAL" and returns a JsonObject with the result.
     *
     * @param target the robot on which the command is executed.
     * @return a JsonObject containing the result of the reload command.
     */

        JsonObject response = new JsonObject();


       if (target.getShots() == target.iShot) {
            return generateErrorResponse("Robot is already fully loaded.");
        } else {
            target.setReload(true);
            target.setStatus("RELOAD");
            int reloadTime = target.getReload();

            try {
                Thread.sleep(reloadTime * 1000L);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            target.setReload(false);
            target.setShots(target.iShot);
            target.setStatus("NORMAL");

            response.addProperty("result", "OK");
            JsonObject jsonData = new JsonObject();
            jsonData.addProperty("message", "Done");
            response.add("data", jsonData);
            response.add("state", target.state());

        }
        return response;
    }
}

