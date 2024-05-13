package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonObject;

public class LookCommand extends Command {

    @Override
    public JsonObject execute(Robot target) {
        JsonObject response = new JsonObject();
        response.addProperty("command","Look");
        response.addProperty("functionality","Shows obstacles");
        return response;
    }
}

