package Server.Robots;

import Server.Server;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Launch {
    private Robot robot;

    public Launch(JsonObject received) {
        String name = received.get("robot").getAsString();
        JsonArray args = received.get("arguments").getAsJsonArray();
        String kind = args.get(0).getAsString();
        int shield = args.get(1).getAsInt();
        int shots = args.get(2).getAsInt();
        Robot robot = new Robot(name,kind,shield,shots);
        this.robot = robot;
        Server.robots.add(robot);
    }

    public JsonObject LaunchResponse(){
        return this.robot.toJSON();
    }

    public Robot getRobot() {
        return robot;
    }
}
