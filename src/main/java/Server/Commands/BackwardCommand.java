package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class BackwardCommand extends Command{

    @Override
    public JsonObject execute(Robot target) {
        JsonObject response = new JsonObject();

        if (target.isRepairing()){
            return generateErrorResponse("Robot is currently repairing and can't move");
        }

        int nrSteps = Integer.parseInt(getArgument().getAsString());
        if (target.updatePosition(-nrSteps)){
            response.addProperty("result","OK");
            JsonObject data = new JsonObject();
            data.addProperty("message","Done");
            response.add("data",data);
            response.add("state",target.state());

        } else {
            response.addProperty("result","OK");
            JsonObject data = new JsonObject();
            data.addProperty("message","Obstructed");
            response.add("data",data);
            response.add("state",target.state());
        }

        return response;
    }
    public BackwardCommand(JsonArray args) {
        super(args);
    }
}