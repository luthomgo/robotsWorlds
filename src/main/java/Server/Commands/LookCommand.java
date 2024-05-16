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
                objects.add(obstaclesJson(distance, target, obstacle));
            }
        }
        // iterate through all the robots in the world
        for (Robot robot : robots){
            if (robot.equals(target)){
                continue;
            }
            // check  if the robot is within the visibility range of other robots
//            Position robotPosition = target.getPosition();
            Position otherRobotPosition = robot.getPosition();
            int xDifference = Math.abs(x - otherRobotPosition.getX());
            int yDifference = Math.abs(y - otherRobotPosition.getY());
            int distance = xDifference + yDifference;
            if (distance <= visibility){
                objects.add(robotsJson(distance, robot, target ));
            }
        }
        response.add("objects", objects );
        response.add("state", target.state());
        return response;
    }

    private Direction getObstacleDirection(int x , int y, int obstacleX, int obstacleY ) {
        if (obstacleY < y){
                return Direction.NORTH;
        }else if (obstacleY > y){
            return Direction.SOUTH;
        }else if (obstacleX < x){
            return Direction.EAST;
        }else{
            return Direction.WEST;
        }

    }

    private Direction getRobotDirection(int x , int y, Position otherRobotPosition ) {
        if (otherRobotPosition.getY() < y){
            return Direction.NORTH;
        }else if (otherRobotPosition.getY() > y){
            return Direction.SOUTH;
        }else if (otherRobotPosition.getX() < x){
            return Direction.EAST;
        }else{
            return Direction.WEST;
        }

    }



    private JsonObject robotsJson(int distance, Robot robot, Robot target) {
        JsonObject jsonObject = new JsonObject();
        Position otherRobotPosition = robot.getPosition();
        Direction direction = getRobotDirection(target.getPosition().getX(), target.getPosition().getY(), otherRobotPosition);
        jsonObject.addProperty("Direction", direction.toString());
        jsonObject.addProperty("type", "robot");
        jsonObject.addProperty("Distance", distance);
        return jsonObject;

    }

    private JsonObject obstaclesJson(int distance, Robot target,SqaureObstacle obstacle) {
        JsonObject jsonObject = new JsonObject();
        Position obstaclePosition = new Position(obstacle.getBottomLeftX(), obstacle.getBottomLeftY());
        Direction direction = getObstacleDirection(target.getPosition().getX(), target.getPosition().getY(), obstaclePosition.getX(), obstaclePosition.getY());
        jsonObject.addProperty("Direction", direction.toString());
        jsonObject.addProperty("type", "obstacle");
        jsonObject.addProperty("Distance", distance);
        return jsonObject;
    }

}

