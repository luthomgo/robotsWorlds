package Server.Commands;

import Server.Robots.Robot;
import com.google.gson.JsonObject;

public class RepairCommand extends Command{
    @Override
    public JsonObject execute(Robot target) {
        target.setStatus("repair");
        target.setRepairing(true);

        int repairTime = target.getRepair();

        try{
            Thread.sleep(repairTime * 1000L);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        target.repairShields();

        target.setStatus("NORMAL");
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
