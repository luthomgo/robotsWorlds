package Server.Commands;

import Server.Robots.Direction;
import Server.Robots.Position;
import Server.Robots.Robot;
import Server.Server;
import Server.World.Obstacles;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;

public class FireCommand extends Command {
    /**
     * The FireCommand class handles the firing action of a robot.
     * It determines if the robots hits an obstacle or another based on its direction and updates the
     * game states accordingly.
     */
    public JsonArray ObstaclesNorth;
    public JsonArray ObstaclesSouth;
    public JsonArray ObstaclesWest;
    public JsonArray ObstaclesEast;

    @Override
    public JsonObject execute(Robot target) {
        /**
         * Executes the fire command for the specified robot.
         * The robot will attempt to fire in its current direction, checking for hits on obstacles or
         * other robots.
         *
         * @param target The robot that is executing the fire command
         * @return A JsonObject containing the result of the fire command execution, including whether
         * it hit another robot, the distance to the target, and the remaining number of shots.
         */
        JsonArray obFinal = generateObFinal(target);
        JsonObject response = new JsonObject();
        response.addProperty("result","ok");

        JsonObject data = new JsonObject();

        String name = "";

        if(target.getDirection() == Direction.NORTH){
            for (JsonElement i:ObstaclesNorth){
                JsonObject obs = i.getAsJsonObject();
                if (obs.get("type").getAsString().equals("Robot")) {
                    name = obs.get("name").getAsString();
                    Robot rob = getRobotObject(name,target);
                    if (target.minusShot()) {
                        rob.minusShield();
                        JsonObject shots = new JsonObject();
                        shots.addProperty("shots",target.getShots());
                        data.addProperty("message","Hit");
                        data.addProperty("distance",obs.get("distance").getAsInt());
                        data.addProperty("robot",name);
                        data.add("state",rob.state());
                        response.add("data",data);
                        response.add("state",shots);
                        return response;
                    }
                }
            }
        }

        if(target.getDirection() == Direction.SOUTH){
            for (JsonElement i:ObstaclesSouth){
                JsonObject obs = i.getAsJsonObject();
                if (obs.get("type").getAsString().equals("Robot")) {
                    name = obs.get("name").getAsString();
                    Robot rob = getRobotObject(name,target);
                    if (target.minusShot()) {
                        rob.minusShield();
                        JsonObject shots = new JsonObject();
                        shots.addProperty("shots",target.getShots());
                        data.addProperty("message","Hit");
                        data.addProperty("distance",obs.get("distance").getAsInt());
                        data.addProperty("robot",name);
                        data.add("state",rob.state());
                        response.add("data",data);
                        response.add("state",shots);
                        return response;
                    }
                }
            }
        }

        if(target.getDirection() == Direction.WEST){
            for (JsonElement i:ObstaclesWest){
                JsonObject obs = i.getAsJsonObject();
                if (obs.get("type").getAsString().equals("Robot")) {
                    name = obs.get("name").getAsString();
                    Robot rob = getRobotObject(name,target);
                    if (target.minusShot()) {
                        rob.minusShield();
                        JsonObject shots = new JsonObject();
                        shots.addProperty("shots",target.getShots());
                        data.addProperty("message","Hit");
                        data.addProperty("distance",obs.get("distance").getAsInt());
                        data.addProperty("robot",name);
                        data.add("state",rob.state());
                        response.add("data",data);
                        response.add("state",shots);
                        return response;
                    }
                }
            }
        }

        if(target.getDirection() == Direction.EAST){
            for (JsonElement i:ObstaclesEast){
                JsonObject obs = i.getAsJsonObject();
                if (obs.get("type").getAsString().equals("Robot")) {
                    name = obs.get("name").getAsString();
                    Robot rob = getRobotObject(name,target);
                    if (target.minusShot()) {
                        rob.minusShield();
                        JsonObject shots = new JsonObject();
                        shots.addProperty("shots",target.getShots());
                        data.addProperty("message","Hit");
                        data.addProperty("distance",obs.get("distance").getAsInt());
                        data.addProperty("robot",name);
                        data.add("state",rob.state());
                        response.add("data",data);
//                        response.add("state",rob.state());
                        response.add("state",shots);
                        return response;
                    }
                }

            }
        }

        if (name.isEmpty() && target.getShots() > 0) {
            target.minusShot();
            data.addProperty("message","Miss");
            response.add("data",data);
            JsonObject shots = new JsonObject();
            shots.addProperty("shots",target.getShots());
            response.add("state",shots);
            return response;
        }


        data.addProperty("message","No more shots left Reload!");
        response.add("data",data);
        JsonObject shots = new JsonObject();
        shots.addProperty("shots",target.getShots());
        response.add("state",shots);
        return response;
    }

    public Robot getRobotObject(String name,Robot target){
        /**
         * Retrieves the robot object with the specified name from the list of robots in the game.
         *
         * @param name The name of the robot to retrieve.
         * @param target The robot is executing the fire command.
         * @return The robot with the specified name, or null if not found.
         */
        for(Robot i : target.getRobotList()){
            if(i.getName().equals(name)){
                return i;
            }
        }
        return null;
    }

    public JsonArray generateObFinal(Robot target){
        /**
         * Generates a JsonArray of obstacles and robots that are within the visibility range of the
         * specified robot.
         *
         * @param target The robot for which to generate the list of the obstacles and robots.
         * @return A sorted JsonArray of obstacles and robots within the visibility range of the specified robot.
         */
        JsonArray objects = new JsonArray();

        int tX = target.getPosition().getX();
        int tY = target.getPosition().getY();

        int visibility = target.getVisibility();
        Position look_north = new Position(tX,tY + visibility);
        Position look_south = new Position(tX,tY - visibility);
        Position look_west = new Position(tX - visibility,tY);
        Position look_east = new Position(tX + visibility,tY);

        for (Obstacles i : target.getObstacles()) {
            JsonObject obstacleStats = new JsonObject();

            if (look_north.getY() >= i.getBottomLeftY() && tY <= i.getBottomLeftY() && tX >= i.getBottomLeftX() && tX <= i.getBottomLeftX() + i.getSize()){
                obstacleStats.addProperty("direction", "NORTH");
                obstacleStats.addProperty("type", i.getType());
                int distance = tY - i.getBottomLeftY();
                if (distance < 0) distance *= -1;
                obstacleStats.addProperty("distance", distance);
                objects.add(obstacleStats);
            }

            if (look_south.getY() <= i.getBottomLeftY() && tY >= i.getBottomLeftY() && tX >= i.getBottomLeftX() && tX<= i.getBottomLeftX() + i.getSize()){
                obstacleStats.addProperty("direction", "SOUTH");
                obstacleStats.addProperty("type", i.getType());
                int distance = tY - i.getBottomLeftY();
                if (distance < 0) distance *= -1;
                obstacleStats.addProperty("distance", distance);
                objects.add(obstacleStats);
            }

            if (look_west.getX() <= i.getBottomLeftX() &&  tX >= i.getBottomLeftX() && tY >= i.getBottomLeftY() && tY <= i.getBottomLeftY() + i.getSize()){
                obstacleStats.addProperty("direction", "WEST");
                obstacleStats.addProperty("type", i.getType());
                int distance = tX - i.getBottomLeftX();
                if (distance < 0) distance *= -1;
                obstacleStats.addProperty("distance", distance);
                objects.add(obstacleStats);
            }

            if (look_east.getX() >= i.getBottomLeftX() && tX <= i.getBottomLeftX() && tY >= i.getBottomLeftY() && tY <= i.getBottomLeftY() + i.getSize()){
                obstacleStats.addProperty("direction", "EAST");
                obstacleStats.addProperty("type", i.getType());
                int distance = tX - i.getBottomLeftX();
                if (distance < 0) distance *= -1;
                obstacleStats.addProperty("distance", distance);
                objects.add(obstacleStats);
            }
        }

        Position currRobot = target.getPosition();
        int robotX = currRobot.getX();
        int robotY = currRobot.getY();

        Position robot_north = new Position(robotX,robotY + visibility);
        Position robot_south = new Position(robotX,robotY - visibility);
        Position robot_west = new Position(robotX - visibility,robotY);
        Position robot_east = new Position(robotX + visibility,robotY);

        List<Robot> rl = target.getRobotList();
        for (Robot robot : rl) {
            if (!target.getName().equals(robot.getName())){
                JsonObject robotStats = new JsonObject();
                Position rob = robot.getPosition();
                int robotObX = robot.getPosition().getX();
                int robotObY = robot.getPosition().getY();
                if (robot_north.getY() > robotObY && robotY < robotObY && robotX >= robotObX && robotX <= robotObX ){
                    robotStats.addProperty("direction","NORTH");
                    robotStats.addProperty("type","Robot");
                    int distance = robotY - robot.getPosition().getY();
                    if (distance < 0) distance *= -1; //remove negative to positive
                    robotStats.addProperty("distance",distance);
                    robotStats.addProperty("name",robot.getName());
                    objects.add(robotStats);

                }
                if (robot_south.getY() < robotObY && robotY > robotObY && robotX >= robotObX && robotX <= robotObX ){
                    robotStats.addProperty("direction","SOUTH");
                    robotStats.addProperty("type","Robot");
                    int distance = robotY - robot.getPosition().getY();
                    if (distance < 0) distance *= -1;
                    robotStats.addProperty("distance",distance);
                    robotStats.addProperty("name",robot.getName());
                    objects.add(robotStats);

                }
                if (robot_east.getX() > robotObX && robotX < robotObX && robotY >= robotObY && robotY <= robotObY){
                    robotStats.addProperty("direction","EAST");
                    robotStats.addProperty("type","Robot");
                    int distance = robotX - robot.getPosition().getX();
                    if (distance < 0) distance *= -1;
                    robotStats.addProperty("distance",distance);
                    robotStats.addProperty("name",robot.getName());
                    objects.add(robotStats);

                }
                if (robot_west.getX() < robotObX && robotX > robotObX && robotY >= robotObY && robotY <= robotObY){
                    robotStats.addProperty("direction","WEST");
                    robotStats.addProperty("type","Robot");
                    int distance = robotX - robot.getPosition().getX();
                    if (distance < 0) distance *= -1;
                    robotStats.addProperty("distance",distance);
                    robotStats.addProperty("name",robot.getName());
                    objects.add(robotStats);
                }
            }

        }

        JsonArray ob = sortArray(objects);
        JsonArray obFinal = new JsonArray();

        JsonArray obNorth = new JsonArray();
        JsonArray obSouth = new JsonArray();
        JsonArray obWest = new JsonArray();
        JsonArray obEast = new JsonArray();

        for (JsonElement i : ob){
            if (i.getAsJsonObject().get("direction").getAsString().equals("NORTH")){
                obNorth.add(i);
                if (i.getAsJsonObject().get("type").getAsString().equals("mountain")) break;
            }
        }
        for (JsonElement i : ob){
            if (i.getAsJsonObject().get("direction").getAsString().equals("SOUTH")){
                obSouth.add(i);
                if (i.getAsJsonObject().get("type").getAsString().equals("mountain")) break;
            }
        }
        for (JsonElement i : ob){
            if (i.getAsJsonObject().get("direction").getAsString().equals("EAST")){
                obEast.add(i);
                if (i.getAsJsonObject().get("type").getAsString().equals("mountain")) break;
            }
        }
        for (JsonElement i : ob){
            if (i.getAsJsonObject().get("direction").getAsString().equals("WEST")){
                obWest.add(i);
                if (i.getAsJsonObject().get("type").getAsString().equals("mountain")) break;
            }
        }
        this.ObstaclesNorth = obNorth;
        this.ObstaclesSouth = obSouth;
        this.ObstaclesEast = obEast;
        this.ObstaclesWest = obWest;

        for (JsonElement i:obNorth){
            obFinal.add(i);
        }
        for (JsonElement i:obSouth){
            obFinal.add(i);
        }
        for (JsonElement i:obWest){
            obFinal.add(i);
        }
        for (JsonElement i:obEast){
            obFinal.add(i);
        }
        return obFinal;
    }

    public JsonArray sortArray(JsonArray objects){
        /**
         * Sorts a JsonArray of objects by their distance property in ascending order.
         *
         * @param objects The JsonArray of objects to sort.
         * @return A sorted JsonArray of objects by their distance property.
         */
        JsonObject smal;

        JsonArray temp;
        JsonArray res = new JsonArray();

        temp = objects;
        while (!temp.isEmpty()) {
            smal = temp.get(0).getAsJsonObject();
            for (int i = 0; i < temp.size(); i++) {
                int smallDis = smal.get("distance").getAsInt();
                JsonObject temf = temp.get(i).getAsJsonObject();
                int tempDistance = temf.get("distance").getAsInt();

                if (tempDistance < smallDis){
                    smal = temp.get(i).getAsJsonObject();
                }
            }
            res.add(smal);
            temp.remove(smal);
        }
        return res;
    }
}
