package Server.Commands;

import Server.Robots.Position;
import Server.Robots.Robot;
import Server.World.Obstacles;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class LookCommand extends Command {
    /**
     * LookCommand is a type of command that scans the surroundings of robots and provides details
     * about the obstacles and other robots within its visibility range
     */

    private JsonArray ObstaclesNorth;
    private JsonArray ObstaclesSouth;
    private JsonArray ObstaclesWest;
    private JsonArray ObstaclesEast;

    @Override
    public JsonObject execute(Robot target) {
        /**
         * Executes the LookCommand for the specified robot. This method scans the surrounding and returns
         * a Json object containing the details of obstacles and other robots within its visibility range.
         *
         * @param target The robot for which the command is executed.
         * @return A Json containing the result of the look command, including detected obstacles and other robots.
         */
        JsonObject response = new JsonObject();
        JsonArray obFinal = generateObFinal(target);
        response.addProperty("result","ok");
        JsonObject data = new JsonObject();
        data.add("objects",obFinal);
        response.add("data",data);
        response.add("state",target.state());
        return response;
    }

    public JsonArray sortArray(JsonArray objects){
        /**
         * Sorts the given JsonArray of objects based on their distance property in ascending order
         *
         * @param objects a jsonArray of objects to be sorted
         * @return A sorted JsonArray of objects based on the distance.
         */
        JsonObject small;

        JsonArray temp;
        JsonArray res = new JsonArray();

        temp = objects;
        while (!temp.isEmpty()) {
            small = temp.get(0).getAsJsonObject();
            for (int i = 0; i < temp.size(); i++) {
                int smallDis = small.get("distance").getAsInt();
                JsonObject tempObj = temp.get(i).getAsJsonObject();
                int tempDistance = tempObj.get("distance").getAsInt();

                if (tempDistance < smallDis){
                    small = temp.get(i).getAsJsonObject();
                }
            }
            res.add(small);
            temp.remove(small);
        }
        return res;
    }

    public JsonArray generateObFinal(Robot target){
        /**
         * Generates a JsonArray containing details of obstacles and other robots within the visibility range
         * of the target robot. It categorises these object based on their direction (NORTH, SOUTH,EAST,WEST).
         *
         * @param target The robot for which the surrounding are being scanned.
         * @return A JsonArray containing the detected objects within the robot's visibility.
         */
        JsonArray objects = new JsonArray();

        int tX = target.getPosition().getX();
        int tY = target.getPosition().getY();

        int visibility = target.getVisibility();

        Position look_north = new Position(tX,tY + visibility);
        Position look_south = new Position(tX,tY - visibility);
        Position look_west = new Position(tX - visibility,tY);
        Position look_east = new Position(tX + visibility,tY);

        // check edges and add them to the objects if within the visibility
        if (look_north.getY() >= target.getTOP_LEFT().getY()){
            JsonObject edgeStats = new JsonObject();
            edgeStats.addProperty("direction", "NORTH");
            edgeStats.addProperty("type", "Edge");
            int distance =  tY - target.getTOP_LEFT().getY();
            if (distance < 0) distance *= -1; //remove negative to positive
            edgeStats.addProperty("distance",distance);
            objects.add(edgeStats);
        }

        if (look_south.getY() <= target.getBOTTOM_RIGHT().getY()){
            JsonObject edgeStats = new JsonObject();
            edgeStats.addProperty("direction", "SOUTH");
            edgeStats.addProperty("type", "Edge");
            int distance = target.getBOTTOM_RIGHT().getY() - tY;
            if (distance < 0) distance *= -1; //remove negative to positive
            edgeStats.addProperty("distance",distance);
            objects.add(edgeStats);
        }

        if (look_east.getX() >= target.getBOTTOM_RIGHT().getX()){
            JsonObject edgeStats = new JsonObject();
            edgeStats.addProperty("direction", "EAST");
            edgeStats.addProperty("type", "Edge");
            int distance = tX - target.getBOTTOM_RIGHT().getX();
            if (distance < 0) distance *= -1; //remove negative to positive
            edgeStats.addProperty("distance",distance);
            objects.add(edgeStats);
        }

        if (look_west.getX() <= target.getTOP_LEFT().getX()){
            JsonObject edgeStats = new JsonObject();
            edgeStats.addProperty("direction", "WEST");
            edgeStats.addProperty("type", "Edge");
            int distance =   target.getTOP_LEFT().getX() - tX;
            if (distance < 0) distance *= -1; //remove negative to positive
            edgeStats.addProperty("distance",distance);
            objects.add(edgeStats);
        }

        // check obstacles and add the to objects if within visibility
        for (Obstacles i : target.getObstacles()) {
            JsonObject obstacleStats = new JsonObject();

            if ((look_north.getY() >= i.getBottomLeftY() || look_north.getY() >= i.getBottomLeftY() + i.getSize()) && tY <= i.getBottomLeftY() && tX >= i.getBottomLeftX() && tX <= i.getBottomLeftX() + i.getSize()){
                obstacleStats.addProperty("direction", "NORTH");
                obstacleStats.addProperty("type", i.getType());

                int distance = 0;
                int distance1 = Math.abs(tY - i.getBottomLeftY());
                int distance2 = Math.abs(tY - (i.getBottomLeftY() + i.getSize()));
                distance = Math.min(distance1,distance2);

                obstacleStats.addProperty("distance", distance);
                objects.add(obstacleStats);
            }

            if ((look_south.getY() <= i.getBottomLeftY() || look_south.getY() <= i.getBottomLeftY() + i.getSize()) && tY >= i.getBottomLeftY() && tX >= i.getBottomLeftX() && tX<= i.getBottomLeftX() + i.getSize()){
                obstacleStats.addProperty("direction", "SOUTH");
                obstacleStats.addProperty("type", i.getType());

                int distance = 0;
                int distance1 = Math.abs(tY - i.getBottomLeftY());
                int distance2 = Math.abs(tY - (i.getBottomLeftY() + i.getSize()));
                distance = Math.min(distance1,distance2);

                obstacleStats.addProperty("distance", distance);
                objects.add(obstacleStats);
            }

            if ((look_west.getX() <= i.getBottomLeftX() || look_west.getX() <= i.getBottomLeftX() + i.getSize()) &&  tX >= i.getBottomLeftX() && tY >= i.getBottomLeftY() && tY <= i.getBottomLeftY() + i.getSize()){
                obstacleStats.addProperty("direction", "WEST");
                obstacleStats.addProperty("type", i.getType());

                int distance = 0;
                int distance1 = Math.abs(tX - i.getBottomLeftX());
                int distance2 = Math.abs(tX - (i.getBottomLeftX() + i.getSize()));
                distance = Math.min(distance1,distance2);

                obstacleStats.addProperty("distance", distance);
                objects.add(obstacleStats);
            }

            if ((look_east.getX() >= i.getBottomLeftX() || look_east.getX() >= i.getBottomLeftX() + i.getSize()) && tX <= i.getBottomLeftX() && tY >= i.getBottomLeftY() && tY <= i.getBottomLeftY() + i.getSize()){
                obstacleStats.addProperty("direction", "EAST");
                obstacleStats.addProperty("type", i.getType());

                int distance = 0;
                int distance1 = Math.abs(tX - i.getBottomLeftX());
                int distance2 = Math.abs(tX - (i.getBottomLeftX() + i.getSize()));
                distance = Math.min(distance1,distance2);

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

        // check for other robots within the visibility
        List<Robot> rl = target.getRobotList();
        for (Robot robot : rl) {
            if (!target.getName().equals(robot.getName())){
                JsonObject robotStats = new JsonObject();
                int robotObX = robot.getPosition().getX();
                int robotObY = robot.getPosition().getY();
                if (robot_north.getY() > robotObY && robotY < robotObY && robotX >= robotObX && robotX <= robotObX ){
                    robotStats.addProperty("direction","NORTH");
                    robotStats.addProperty("type","Robot");

                    int distance = 0;
                    int distance1 = Math.abs(robotY - robot.getPosition().getY());
                    int distance2 = Math.abs(robotY - (robot.getPosition().getY() + 1));
                    distance = Math.min(distance1,distance2);

                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);
                }

                if (robot_south.getY() < robotObY && robotY > robotObY && robotX >= robotObX && robotX <= robotObX ){
                    robotStats.addProperty("direction","SOUTH");
                    robotStats.addProperty("type","Robot");

                    int distance = 0;
                    int distance1 = Math.abs(robotY - robot.getPosition().getY());
                    int distance2 = Math.abs(robotY - (robot.getPosition().getY() + 1));
                    distance = Math.min(distance1,distance2);

                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);

                }

                if (robot_east.getX() > robotObX && robotX < robotObX && robotY >= robotObY && robotY <= robotObY){
                    robotStats.addProperty("direction","EAST");
                    robotStats.addProperty("type","Robot");

                    int distance = 0;
                    int distance1 = Math.abs(robotX - robot.getPosition().getX());
                    int distance2 = Math.abs(robotX - (robot.getPosition().getX() + 1));
                    distance = Math.min(distance1,distance2);

                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);
                }

                if (robot_west.getX() < robotObX && robotX > robotObX && robotY >= robotObY && robotY <= robotObY){
                    robotStats.addProperty("direction","WEST");
                    robotStats.addProperty("type","Robot");

                    int distance = 0;
                    int distance1 = Math.abs(robotX - robot.getPosition().getX());
                    int distance2 = Math.abs(robotX - (robot.getPosition().getX() + 1));
                    distance = Math.min(distance1,distance2);

                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);
                }
            }
        }

        // sort object and categorise them by direction
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

    public JsonArray getObstaclesNorth() {
        /**
         * Gets obstacles detected to the north of the robot
         *
         * @return A JsonArray of obstacles to the north
         */
        return ObstaclesNorth;
    }

    public JsonArray getObstaclesSouth() {
        /**
         * Gets obstacles detected to the south of the robot
         *
         * @return A JsonArray of obstacles to the south
         */
        return ObstaclesSouth;
    }

    public JsonArray getObstaclesWest() {
        /**
         * Gets obstacles detected to the west of the robot
         *
         * @return A JsonArray of obstacles to the west
         */
        return ObstaclesWest;
    }

    public JsonArray getObstaclesEast() {
        /**
         * Gets obstacles detected to the east of the robot
         *
         * @return A JsonArray of obstacles to the east
         */
        return ObstaclesEast;
    }
}
