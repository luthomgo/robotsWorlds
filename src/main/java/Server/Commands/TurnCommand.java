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

        if (turnDirection.equalsIgnoreCase("right")) {
            target.updateDirection(true);
            JsonObject data = new JsonObject();
            response.addProperty("result", "ok");
            JsonObject jsondata = new JsonObject();
            jsondata.addProperty("message", "done");
            response.add("data", jsondata);
            response.add("state", target.state());

        } else if (turnDirection.equalsIgnoreCase("left")) {
            target.updateDirection(true);
            JsonObject data = new JsonObject();
            response.addProperty("result", "ok");
            JsonObject jsondata = new JsonObject();
            jsondata.addProperty("message", "done");
            response.add("data", jsondata);
            response.add("state", target.state());

        }

        return response;

    }

}