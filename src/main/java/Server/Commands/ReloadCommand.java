package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ReloadCommand extends Command {



    @Override
    public JsonObject execute(Robot target) {
        JsonObject response = new JsonObject();

        if (target.isReloading()) {
            response.addProperty("result", "ERROR");
            response.addProperty("message", "Robot is already reloading.");
        } else if (target.getShots() == target.getMaxShots()) {
            response.addProperty("result", "ERROR");
            response.addProperty("message", "Robot is already fully loaded.");
        } else {
            target.startReloading();
            response.addProperty("result", "OK");
            JsonObject jsonData = new JsonObject();
            jsonData.addProperty("message", "Done");
            response.add("data", jsonData);
            response.add("state", target.state());
        }

        return response;
    }
}
