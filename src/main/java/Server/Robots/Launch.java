package Server.Robots;

import Server.Server;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Launch {
    private Robot robot;

    public Launch(JsonObject received) {
        String name = received.get("robot").getAsString();
        JsonArray args = received.get("arguments").getAsJsonArray();


        if (!args.isEmpty())
        {
            if (args.get(0).toString().contains("tank")){
                this.robot = new Robot(name,"tank",10,5,5);
            }
            else if (args.get(0).toString().contains("sniper")){
                this.robot = new Robot(name,"sniper",2,3,15);
            }
            else if (args.get(0).toString().contains("brad1")){
                this.robot = new Robot(name,"brad1",5,10,10);
            }
            else {
                this.robot = new Robot(name,"Normy",6,6,6);
            }
        }
        else
        {
            this.robot = new Robot(name,"Normy",6,6,6);
        }
    }

    public JsonObject LaunchResponse()
    {
        return this.robot.toJSON();
    }

    public Robot getRobot() {
        return robot;
    }
}
