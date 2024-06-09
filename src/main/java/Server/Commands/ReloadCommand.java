package Server.Commands;

import Server.Robots.*;
import com.google.gson.JsonObject;

public class ReloadCommand extends Command {


    @Override
    public JsonObject execute(Robot target) {
        JsonObject response = new JsonObject();

        if (target.isReloading()) {
            return generateErrorResponse("Robot is already reloading.");
        } else if (target.getShots() == target.iShot) {
            return generateErrorResponse("Robot is already fully loaded.");
        } else {

            response.addProperty("result", "OK");
            JsonObject jsonData = new JsonObject();
            jsonData.addProperty("message", "Done");
            response.add("data", jsonData);
            response.add("state", target.state());

            target.setReload(true);
            target.setStatus("RELOAD");
            int reloadTime = target.getReload();

            try {
                Thread.sleep(reloadTime * 1000L);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            target.setShots(target.iShot);
            target.setStatus("NORMAL");
            target.setReload(false);
        }
        return response;
    }
}

