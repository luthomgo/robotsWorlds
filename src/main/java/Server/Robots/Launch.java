package Server.Robots;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;

public class Launch {
    private final Robot robot;

    public Launch(JsonObject received, Socket s, DataOutputStream dos,DataInputStream dis) {
        String name = received.get("robot").getAsString();
        JsonArray args = received.get("arguments").getAsJsonArray();

        if (!args.isEmpty())
        {
            if (args.get(0).toString().contains("tank")){
                this.robot = new Robot(name,"tank",5,1,5,s,dos,dis);
            }
            else if (args.get(0).toString().contains("sniper")){
                this.robot = new Robot(name,"sniper",1,5,10,s,dos,dis);
            }
            else if (args.get(0).toString().contains("brad1")){
                this.robot = new Robot(name,"brad1",4,2,5,s,dos,dis);
            }
            else {
                this.robot = new Robot(name,"Normy",3,3,5,s,dos,dis);
            }
        }
        else
        {
            this.robot = new Robot(name,"Normy",6,6,6,s,dos,dis);
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
