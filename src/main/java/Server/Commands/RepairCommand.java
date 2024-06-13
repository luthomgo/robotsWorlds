package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonObject;

public class RepairCommand extends Command {
    @Override
    public JsonObject execute(Robot target) {
        target.setStatus("REPAIR");
        target.setRepairing(true);


        if (target.getIShield() == target.getShield()) {
            return generateErrorResponse("Robot is already repaired");
        } else {
            int repairTime = target.getRepair();
            try {
                Thread.sleep(repairTime * 1000L);
                target.repairShields();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            target.setRepairing(false);
            target.setStatus("NORMAL");

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
}
