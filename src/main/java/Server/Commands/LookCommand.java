package Server.Commands;

import Server.Robots.Direction;
import Server.Robots.Position;
import Server.Robots.Robot;
import Server.World.SqaureObstacle;
import Server.World.World;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class LookCommand extends Command {
    private int visibility;
    private Position position;
    private Direction direction;
    private World world;
    private List<SqaureObstacle> obstacles;
    private List<Robot> robots;

    public LookCommand(World world){
        this.world =  world;

    }

    @Override
    public JsonObject execute(Robot target) {

        JsonObject response = new JsonObject();
        response.addProperty("result","OK");
//        response.addProperty("command","Look");
//        response.addProperty("functionality","Shows obstacles");

        JsonArray objects = new JsonArray();

        this.visibility = target.getVisibility();
        this.position = target.getPosition();
        this.direction = target.getDirection();
        this.obstacles = world.getObstacles();
        this.robots= world.getRobotList();

        int x = position.getX();
        int y = position.getY();

        for (SqaureObstacle obstacle : obstacles){
            //get the position of the obstacles
            int obstacleX = obstacle.getBottomLeftX();
            int obstacleY = obstacle.getBottomLeftY();

            //check if the obstacles is within the visibility range of the robot
//            if (Math.abs(x - obstacleX) <= visibility && Math.abs(y - obstacleY) <= visibility){
            int distance = Math.abs(x - obstacleX) + Math.abs(y - obstacleY);
            if (distance <= visibility){
                objects.add(obstaclesJson(distance, target));
            }
        }
        response.add("objects", objects);
        // iterate through all the robots in the world
        for (Robot robot : robots){
            if (robot.equals(target)){

            }
            // check  if the robot is within the visibility range of other robots
            Position robotPosition = target.getPosition();
            Position otherRobotPosition =robot.getPosition();
            int xDifference = Math.abs(robotPosition.getX() - otherRobotPosition.getX());
            int yDifference = Math.abs(robotPosition.getY() - otherRobotPosition.getY());
            int distance = xDifference + yDifference;
            if (distance <= visibility){
                objects.add(robotsJson(distance, robot, target ));
            }
        }
        response.add("object", objects );
        response.add("state", target.state());
        return response;
    }

    private Direction getDirection(Position robotPosition,Position otherRobotPosition) {
        for (Direction dir : Direction.values()) {
            int x = robotPosition.getX() - otherRobotPosition.getX();
            int y = robotPosition.getY() - otherRobotPosition.getY();
            if (x == 0 && y < 0){
                return Direction.NORTH;
            }
            else if (x == 0 && y > 0){
                return
            }
            else if (dir == Direction.EAST){
                x = position.getX() - visibility;
                y = position.getY();
            }
            else {
                x = position.getX() + visibility;
                y = position.getY();
            }

        }
    }



    private JsonObject robotsJson(int distance, Robot robot, Robot target) {
        JsonObject JsonObject = new JsonObject();
//        JsonObject.addProperty("Direction", target.ge);
        JsonObject.addProperty("type", "robot");
        JsonObject.addProperty("Distance", distance);
        return JsonObject;

    }

    private JsonObject obstaclesJson(int distance, Robot target) {
        JsonObject JsonObject = new JsonObject();
//        JsonObject.addProperty("Direction", );
        JsonObject.addProperty("type", "obstacle");
        JsonObject.addProperty("Distance", distance);
        return JsonObject;
    }

}

