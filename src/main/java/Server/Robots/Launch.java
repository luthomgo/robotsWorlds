package Server.Robots;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;

public class Launch {
    /**
     * The Launch class is responsible for creating a new Robot instance based on the provided JSON data.
     */
    private final Robot robot;

    public Launch(JsonObject received, Socket s, DataOutputStream dos,DataInputStream dis) {
        /**
         * Constructs a Launch object and initializes a Robot based on the provided JSON data.
         *
         * @param received the JSON object containing the robot details and arguments
         * @param s the socket connection
         * @param dos the data output stream for sending data to the socket
         * @param dis the data input stream for receiving data from the socket
         */
        String name = received.get("robot").getAsString();
        JsonArray args = received.get("arguments").getAsJsonArray();

        if (!args.isEmpty())
        {
            if (args.get(0).toString().contains("tank")){
                this.robot = new Robot(name,"tank",5,1,5,s,dos,dis);
            }
            else if (args.get(0).toString().contains("sniper")){
                this.robot = new Robot(name,"sniper",1,1,15,s,dos,dis);
            }
            else if (args.get(0).toString().contains("brad1")){
                this.robot = new Robot(name,"brad1",1,5,5,s,dos,dis);
            }
            else {
                this.robot = new Robot(name,"Normy",3,3,5,s,dos,dis);
            }
        }
        else
        {
            this.robot = new Robot(name,"Normy",3,3,5,s,dos,dis);
        }
    }

    public JsonObject LaunchResponse()
    {
        /**
         * Returns a JSON representation of the initialized robot.
         *
         * @return a JSON object representing the robot's state
         */

        return this.robot.toJSON();
    }

    public Robot getRobot() {
        /**
         * Gets the robot initialized by this Launch object.
         *
         * @return the initialized robot
         */
        return robot;
    }
}
