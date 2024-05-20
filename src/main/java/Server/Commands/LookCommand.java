package Server.Commands;

import Server.Robots.Direction;
import Server.Robots.Position;
import Server.Robots.Robot;
import Server.Server;
import Server.World.Obstacles;
import Server.World.SqaureObstacle;
import Server.World.World;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class LookCommand extends Command {

    @Override
    public JsonObject execute(Robot target) {
        JsonArray objects = new JsonArray();
        JsonObject response = new JsonObject();

        int tX = target.getPosition().getX();
        int tY = target.getPosition().getY(); //y of robot

        int visibility = target.getVisibility();
        Position look_north = new Position(tX,tY - visibility);
        Position look_south = new Position(tX,tY + visibility);
        Position look_west = new Position(tX - visibility,tY);
        Position look_east = new Position(tX + visibility,tY);

        Position curPos = target.getPosition();

        for (SqaureObstacle i : Server.world.obstacles){
            JsonObject obstacleStats = new JsonObject();
            Position ob = new Position(i.getBottomLeftX(), i.getBottomLeftY());
            if (i.blocksPath(curPos,look_north)){
                obstacleStats.addProperty("direction","NORTH");
                obstacleStats.addProperty("type","Square");
                int distance = tY - i.getBottomLeftY();
                if (distance < 0) distance *= -1; //remove negative to positive
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);
            }

            if (i.blocksPath(curPos,look_south)){
                obstacleStats.addProperty("direction","SOUTH");
                obstacleStats.addProperty("type","Square");
                int distance = tY - i.getBottomLeftY();
                if (distance < 0) distance *= -1;
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);
            }

            if (i.blocksPath(curPos,look_west)){
                obstacleStats.addProperty("direction","WEST");
                obstacleStats.addProperty("type","Square");
                int distance = tX - i.getBottomLeftX();
                if (distance < 0) distance *= -1; //remove negative to positive
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);
            }
            if (i.blocksPath(curPos,look_east)){
                obstacleStats.addProperty("direction","EAST");
                obstacleStats.addProperty("type","Square");
                int distance = tX - i.getBottomLeftX();
                if (distance < 0) distance *= -1; //remove negative to positive
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);
            }

        }
        Position currRobot = target.getPosition();
        int robotX = currRobot.getX();
        int robotY = currRobot.getY();

        Position robot_north = new Position(robotX,robotY - visibility);
        Position robot_south = new Position(robotX,robotY + visibility);
        Position robot_west = new Position(robotX - visibility,robotY);
        Position robot_east = new Position(robotX + visibility,robotY);

        for (Robot robot : Server.world.getRobotList()) {
            if (robot.equals(target)) {
                continue;
            }
            JsonObject obstacleStats = new JsonObject();
            Position rob = robot.getPosition();
            SqaureObstacle robObstacle = new SqaureObstacle(rob.getX(), rob.getY());
            if (robObstacle.blocksPath(rob, robot_north)){
                obstacleStats.addProperty("direction","NORTH");
                obstacleStats.addProperty("type","Robot");
                int distance = robotY - robot.getPosition().getY();
                if (distance < 0) distance *= -1; //remove negative to positive
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);

            }if (robObstacle.blocksPath(rob, robot_south)){
                obstacleStats.addProperty("direction","SOUTH");
                obstacleStats.addProperty("type","Robot");
                int distance = robotY - robot.getPosition().getY();
                if (distance < 0) distance *= -1; //remove negative to positive
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);

            }if (robObstacle.blocksPath(rob, robot_east)){
                obstacleStats.addProperty("direction","EAST");
                obstacleStats.addProperty("type","Robot");
                int distance = robotX - robot.getPosition().getX();
                if (distance < 0) distance *= -1; //remove negative to positive
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);

            }if (robObstacle.blocksPath(rob, robot_west)){
                obstacleStats.addProperty("direction","WEST");
                obstacleStats.addProperty("type","Robot");
                int distance = robotX - robot.getPosition().getX();
                if (distance < 0) distance *= -1; //remove negative to positive
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);
            }

        }

        response.addProperty("result","ok");

        JsonObject data = new JsonObject();
        data.add("objects",objects);




        response.add("data",data);

        response.add("state",target.state());
        return response;
    }

}
