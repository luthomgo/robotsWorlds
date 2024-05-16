package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonObject;

public class StateCommand extends Command {

    @Override
    public JsonObject execute(Robot target) {
        JsonObject response = target.state();
        return response;
    }
}
