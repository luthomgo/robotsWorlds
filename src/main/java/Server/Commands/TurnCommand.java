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
        JsonArray arguments = getArgument();
        String turnDirection = arguments.get(0).getAsString();
        JsonObject response = new JsonObject();

        if (target.isRepairing()){
            return generateErrorResponse("Robot is currently repairing and can't move");
        }
        else if (target.isReloading()) {
            return generateErrorResponse("Robot is currently repairing and can't move");
        }

        if (turnDirection.equalsIgnoreCase("right")) {
            target.updateDirection(true);
            JsonObject data = new JsonObject();
            response.addProperty("result", "ok");
            JsonObject jsonData = new JsonObject();
            jsonData.addProperty("message", "done");
            response.add("data", jsonData);
            response.add("state", target.state());

        } else if (turnDirection.equalsIgnoreCase("left")) {
            target.updateDirection(true);
            JsonObject data = new JsonObject();
            response.addProperty("result", "ok");
            JsonObject jsonData = new JsonObject();
            jsonData.addProperty("message", "done");
            response.add("data", jsonData);
            response.add("state", target.state());

        }
        return response;
    }
}