package Server.Commands;

import Server.Robots.Position;
import Server.Robots.Robot;
import Server.Server;
import Server.World.Obstacles;
import Server.World.SqaureObstacle;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

        for (Obstacles i : Server.world.obstacles){
            JsonObject obstacleStats = new JsonObject();
            Position ob = new Position(i.getBottomLeftX(), i.getBottomLeftY());
            if (i.blocksPath(curPos,look_north)){
                obstacleStats.addProperty("direction","NORTH");
                obstacleStats.addProperty("type",i.getType());
                int distance = tY - i.getBottomLeftY();
                if (distance < 0) distance *= -1; //remove negative to positive
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);
            }

            if (i.blocksPath(curPos,look_south)){
                obstacleStats.addProperty("direction","SOUTH");
                obstacleStats.addProperty("type",i.getType());
                int distance = tY - i.getBottomLeftY();
                if (distance < 0) distance *= -1;
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);
            }

            if (i.blocksPath(curPos,look_west)){
                obstacleStats.addProperty("direction","WEST");
                obstacleStats.addProperty("type",i.getType());
                int distance = tX - i.getBottomLeftX();
                if (distance < 0) distance *= -1; //remove negative to positive
                obstacleStats.addProperty("distance",distance);
                objects.add(obstacleStats);
            }
            if (i.blocksPath(curPos,look_east)){
                obstacleStats.addProperty("direction","EAST");
                obstacleStats.addProperty("type",i.getType());
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
            if (!target.getName().equals(robot.getName())){
                JsonObject robotStats = new JsonObject();
                Position rob = robot.getPosition();
                SqaureObstacle robObstacle = new SqaureObstacle(rob.getX(), rob.getY());
                if (robObstacle.blocksPath(target.getPosition(), robot_north)){
                    robotStats.addProperty("direction","NORTH");
                    robotStats.addProperty("type","Robot");
                    int distance = robotY - robot.getPosition().getY();
                    if (distance < 0) distance *= -1; //remove negative to positive
                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);

                }if (robObstacle.blocksPath(target.getPosition(), robot_south)){
                    robotStats.addProperty("direction","SOUTH");
                    robotStats.addProperty("type","Robot");
                    int distance = robotY - robot.getPosition().getY();
                    if (distance < 0) distance *= -1; //remove negative to positive
                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);

                }if (robObstacle.blocksPath(target.getPosition(), robot_east)){
                    robotStats.addProperty("direction","EAST");
                    robotStats.addProperty("type","Robot");
                    int distance = robotX - robot.getPosition().getX();
                    if (distance < 0) distance *= -1; //remove negative to positive
                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);

                }if (robObstacle.blocksPath(target.getPosition(), robot_west)){
                    robotStats.addProperty("direction","WEST");
                    robotStats.addProperty("type","Robot");
                    int distance = robotX - robot.getPosition().getX();
                    if (distance < 0) distance *= -1; //remove negative to positive
                    robotStats.addProperty("distance",distance);
                    objects.add(robotStats);
                }
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
