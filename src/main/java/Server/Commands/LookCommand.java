package Server.Commands;

import Server.Robots.Position;
import Server.Robots.Robot;
import Server.Server;
import Server.World.Obstacles;
import Server.World.SqaureObstacle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class LookCommand extends Command {
    private JsonArray ObstaclesNorth;
    private JsonArray ObstaclesSouth;
    private JsonArray ObstaclesWest;
    private JsonArray ObstaclesEast;

    @Override
    public JsonObject execute(Robot target) {
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
        JsonObject smal;

        JsonArray temp = new JsonArray();
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
    public JsonArray generateObFinal(Robot target){
        JsonArray objects = new JsonArray();


        int tX = target.getPosition().getX();
        int tY = target.getPosition().getY();

        int visibility = target.getVisibility();
        Position look_north = new Position(tX,tY + visibility);
        Position look_south = new Position(tX,tY - visibility);
        Position look_west = new Position(tX - visibility,tY);
        Position look_east = new Position(tX + visibility,tY);


        //edge
        if (look_north.getY() >= Server.world.getTOP_LEFT().getY()){
            System.out.println(Server.world.getTOP_LEFT().getY());
            JsonObject edgeStats = new JsonObject();
            edgeStats.addProperty("direction", "NORTH");
            edgeStats.addProperty("type", "Edge");
            int distance =  tY - Server.world.getTOP_LEFT().getY();
            if (distance < 0) distance *= -1; //remove negative to positive
            edgeStats.addProperty("distance",distance);
            objects.add(edgeStats);

        }
        if (look_south.getY() <= Server.world.getBOTTOM_RIGHT().getY()){
            JsonObject edgeStats = new JsonObject();
            edgeStats.addProperty("direction", "SOUTH");
            edgeStats.addProperty("type", "Edge");
            int distance = Server.world.getBOTTOM_RIGHT().getY() - tY;
            if (distance < 0) distance *= -1; //remove negative to positive
            edgeStats.addProperty("distance",distance);
            objects.add(edgeStats);

        }
        if (look_east.getX() >= Server.world.getBOTTOM_RIGHT().getX()){
            JsonObject edgeStats = new JsonObject();
            edgeStats.addProperty("direction", "EAST");
            edgeStats.addProperty("type", "Edge");
            int distance = tX - Server.world.getTOP_LEFT().getX();
            if (distance < 0) distance *= -1; //remove negative to positive
            edgeStats.addProperty("distance",distance);
            objects.add(edgeStats);
        }
        if (look_west.getX() <= Server.world.getTOP_LEFT().getX()){
            JsonObject edgeStats = new JsonObject();
            edgeStats.addProperty("direction", "WEST");
            edgeStats.addProperty("type", "Edge");
            int distance =   Server.world.getBOTTOM_RIGHT().getX() - tX;
            if (distance < 0) distance *= -1; //remove negative to positive
            edgeStats.addProperty("distance",distance);
            objects.add(edgeStats);

        }

        Position curPos = target.getPosition();
        for (Obstacles i : Server.world.obstacles) {
            JsonObject obstacleStats = new JsonObject();
            Position ob = new Position(i.getBottomLeftX(), i.getBottomLeftY());

            if (look_north.getY() >= i.getBottomLeftY() && tY <= i.getBottomLeftY() && tX >= i.getBottomLeftX() && tX <= i.getBottomLeftX() + i.getSize()){
//                System.out.println("not");
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

        List<Robot> rl = Server.world.robotList;
        for (Robot robot : rl) {
            if (!target.getName().equals(robot.getName())){
                JsonObject robotStats = new JsonObject();
                Position rob = robot.getPosition();
                SqaureObstacle robObstacle = new SqaureObstacle(rob.getX(), rob.getY());
                int robotObX = robot.getPosition().getX();
                int robotObY = robot.getPosition().getY();
                if (robot_north.getY() > robotObY && robotY < robotObY && robotX >= robotObX && robotX <= robotObX ){
                    robotStats.addProperty("direction","NORTH");
                    robotStats.addProperty("type","Robot");
                    int distance = robotY - robot.getPosition().getY();
                    if (distance < 0) distance *= -1; //remove negative to positive
                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);

                }
                if (robot_south.getY() < robotObY && robotY > robotObY && robotX >= robotObX && robotX <= robotObX ){
                    robotStats.addProperty("direction","SOUTH");
                    robotStats.addProperty("type","Robot");
                    int distance = robotY - robot.getPosition().getY();
                    if (distance < 0) distance *= -1; //remove negative to positive
                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);

                }
                if (robot_east.getX() > robotObX && robotX < robotObX && robotY >= robotObY && robotY <= robotObY){
                    robotStats.addProperty("direction","EAST");
                    robotStats.addProperty("type","Robot");
                    int distance = robotX - robot.getPosition().getX();
                    if (distance < 0) distance *= -1; //remove negative to positive
                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);

                }
                if (robot_west.getX() < robotObX && robotX > robotObX && robotY >= robotObY && robotY <= robotObY){
                    robotStats.addProperty("direction","WEST");
                    robotStats.addProperty("type","Robot");
                    int distance = robotX - robot.getPosition().getX();
                    if (distance < 0) distance *= -1; //remove negative to positive
                    robotStats.addProperty("distance",distance);
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

    public JsonArray getObstaclesNorth() {
        return ObstaclesNorth;
    }

    public JsonArray getObstaclesSouth() {
        return ObstaclesSouth;
    }

    public JsonArray getObstaclesWest() {
        return ObstaclesWest;
    }

    public JsonArray getObstaclesEast() {
        return ObstaclesEast;
    }
}
