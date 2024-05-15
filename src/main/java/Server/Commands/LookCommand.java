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

    @Override
    public JsonObject execute(Robot target) {

        JsonObject response = new JsonObject();
        response.addProperty("result","OK");
        response.addProperty("command","Look");
        response.addProperty("functionality","Shows obstacles");

        JsonArray objects = new JsonArray();

//        this.world = world;
        this.visibility = target.getVisibility();
        this.position = target.getPosition();
        this.direction = target.getDirection();
        this.obstacles = world.getObstacles();
        this.robot = world.get

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
                objects.add(obstaclesJson(distance, obstacle, target, direction));
            }
        }
        response.add("objects", objects);
        // iterate through all the robots in the world
        for (Robot robot :  )
        return response;
    }

    private String obstaclesJson(int distance, SqaureObstacle obstacle, Robot target, Direction direction) {
    }

}

