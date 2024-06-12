package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonObject;

public class ExitCommand extends Command {

    @Override
    public JsonObject execute(Robot target) {
        JsonObject response = new JsonObject();
        target.removeRobot(target);
        response.addProperty("result","OK");
        JsonObject data = new JsonObject();
        data.addProperty("message","exit");
        return response;
    }
}
