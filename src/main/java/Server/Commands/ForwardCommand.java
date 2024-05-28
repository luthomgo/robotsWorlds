package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

        public class ForwardCommand extends Command{



            @Override
            public JsonObject execute(Robot target) {
                if (target.isRepairing()){
                    return generateErrorResponse("Robot is currently repairing and can't move");
                }else {
                    JsonObject response = new JsonObject();
                    response.addProperty("Command","Forward");
                    response.add("Arguments",getArgument());
                    return response;
                }
            }
            public ForwardCommand(JsonArray args) {
                super(args);

    }
}