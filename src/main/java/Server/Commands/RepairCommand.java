package Server.Commands;

import Server.Robots.Robot;
import Server.Server;
import com.google.gson.JsonObject;

public class RepairCommand extends Command{
    @Override
    public JsonObject execute(Robot target) {
        target.setStatus("REPAIR");
        target.setRepairing(true);

        int repairTime = target.getRepair();

        if (target.iShield == Server.world.getMaxShield()){
            JsonObject response = new JsonObject();
            response.addProperty("result", "OK");
            JsonObject data = new JsonObject();
            data.addProperty("message", "Robot is already repaired");
            response.add("data", data);
            JsonObject state = target.state();
            response.add("state", state);
            return response;

        }
        try{
            Thread.sleep(repairTime * 1000L);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        target.repairShields();

        target.setRepairing(false);

        JsonObject response = new JsonObject();
        response.addProperty("result", "OK");
        JsonObject data = new JsonObject();
        data.addProperty("message", "Done");
        response.add("data", data);
        JsonObject state = target.state();
        response.add("state", state);
        return response;
    }
}
