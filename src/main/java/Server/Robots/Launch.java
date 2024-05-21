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
                Robot robot = new Robot(name,"tank",10,5);
                this.robot = robot;
            }
            else if (args.get(0).toString().contains("sniper")){
                System.out.println("sniper selected");
                Robot robot = new Robot(name,"sniper",2,3);
                this.robot = robot;
            }
            else if (args.get(0).toString().contains("brad1")){
                Robot robot = new Robot(name,"brad1",5,10);
                this.robot = robot;
            }
            else {
                Robot robot = new Robot(name,"Normy",6,6);
                this.robot = robot;
            }
        }
        else
        {
            Robot newRobot = new Robot(name,"Normy",6,6);
            this.robot = newRobot;
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
